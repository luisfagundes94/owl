package com.luisfagundes.device.data.repository

import android.net.ConnectivityManager
import com.luisfagundes.device.domain.model.Device
import com.luisfagundes.device.domain.repository.DeviceRepository
import com.luisfagundes.network.calculateSubnet
import com.luisfagundes.network.generateIpRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetAddress
import javax.inject.Inject

private const val TIME_OUT_MILLIS = 500

class DeviceRepositoryImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
): DeviceRepository {

    override fun scanDevices(): Flow<List<Device>> = flow {
        val network = connectivityManager.activeNetwork
        val linkProps = connectivityManager.getLinkProperties(network)
        val ipv4Info = linkProps?.linkAddresses
            ?.firstOrNull { it.address?.hostAddress?.contains('.') == true }

        if (ipv4Info == null) {
            emit(emptyList())
            return@flow
        }

        val localIp = ipv4Info.address.hostAddress ?: run {
            emit(emptyList())
            return@flow
        }
        val prefix = ipv4Info.prefixLength
        val subnet = calculateSubnet(localIp, prefix)
        val ipsToScan = generateIpRange(subnet, prefix)

        val devicesFound = mutableListOf<Device>()

        for (ip in ipsToScan) {
            if (InetAddress.getByName(ip).isReachable(TIME_OUT_MILLIS)) {
                val hostName = try {
                    InetAddress.getByName(ip).canonicalHostName
                } catch (_: Exception) { "" }

                devicesFound += Device(ip, hostName)
            }
            emit(devicesFound.toList())
        }
    }.flowOn(Dispatchers.IO)
}