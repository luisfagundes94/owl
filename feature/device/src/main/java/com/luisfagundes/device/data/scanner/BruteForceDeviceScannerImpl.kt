package com.luisfagundes.device.data.scanner

import android.Manifest
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import com.luisfagundes.domain.model.Device
import java.net.InetAddress
import javax.inject.Inject
import kotlin.collections.iterator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

private const val TIME_OUT_MILLIS = 300
private const val PARALLELISM = 50

internal class BruteForceDeviceScannerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : DeviceScanner {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scanDispatcher = Dispatchers.IO.limitedParallelism(PARALLELISM)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun scanAll(): List<Device> {
        val network = connectivityManager.activeNetwork
        val linkProps = connectivityManager.getLinkProperties(network)
        val ipv4Info = linkProps?.linkAddresses
            ?.firstOrNull { it.address?.hostAddress?.contains('.') == true }

        if (ipv4Info == null) return emptyList()

        val localIp = ipv4Info.address.hostAddress ?: run { return emptyList() }

        val prefix = ipv4Info.prefixLength
        val subnet = calculateSubnet(localIp, prefix)
        val ipsToScan = generateIpRange(subnet, prefix)

        val devicesFound = mutableListOf<Device>()

        coroutineScope {
            val deferredMap = getDeferredPingMap(ipsToScan)
            devicesFound.addAll(getReachableDevices(deferredMap))
        }

        return devicesFound
    }

    private fun CoroutineScope.getDeferredPingMap(
        ipsToScan: List<String>
    ): Map<String, Deferred<Boolean>> {
        val deferredMap: Map<String, Deferred<Boolean>> = ipsToScan.associateWith { ip ->
            async(scanDispatcher) {
                try {
                    val address = InetAddress.getByName(ip)
                    address.isReachable(TIME_OUT_MILLIS)
                } catch (_: Exception) {
                    false
                }
            }
        }
        return deferredMap
    }

    private suspend fun getReachableDevices(
        deferredMap: Map<String, Deferred<Boolean>>
    ): List<Device> {
        val devicesFound = mutableListOf<Device>()

        for ((ip, deferred) in deferredMap) {
            if (deferred.await()) {
                val hostName = try {
                    InetAddress.getByName(ip).canonicalHostName
                } catch (_: Exception) {
                    ""
                }
                devicesFound += Device(
                    ipAddress = ip,
                    hostName = hostName,
                    isActive = true
                )
            }
        }

        return devicesFound
    }
}
