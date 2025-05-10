package com.luisfagundes.network

fun calculateSubnet(ip: String, prefix: Int): String {
    // 1. Parse IP to 32-bit int
    val octets = ip.split('.').map { it.toInt() }
    var ipInt = 0
    for (octet in octets) {
        ipInt = (ipInt shl 8) or (octet and 0xFF)
    }

    // 2. Build mask: e.g. prefix=24 → mask=0xFFFFFF00
    val mask = if (prefix == 0) 0 else (-1 shl (32 - prefix))
    // 3. Compute network address
    val networkInt = ipInt and mask

    // 4. Convert back to dotted-decimal
    return listOf(
        (networkInt shr 24) and 0xFF,
        (networkInt shr 16) and 0xFF,
        (networkInt shr 8) and 0xFF,
        networkInt and 0xFF
    ).joinToString(".")
}

fun generateIpRange(subnet: String, prefix: Int): List<String> {
    // 1. Parse subnet base to int
    val octets = subnet.split('.').map { it.toInt() }
    var baseInt = 0
    for (octet in octets) {
        baseInt = (baseInt shl 8) or (octet and 0xFF)
    }

    // 2. Calculate usable host count (exclude network & broadcast)
    val hostBits = 32 - prefix
    val count = if (hostBits <= 1) 0 else (1 shl hostBits) - 2

    // 3. Generate each host IP: base + offset (1..count)
    return (1..count).map { offset ->
        val ipInt = baseInt + offset
        listOf(
            (ipInt shr 24) and 0xFF,
            (ipInt shr 16) and 0xFF,
            (ipInt shr 8) and 0xFF,
            ipInt and 0xFF
        ).joinToString(".")
    }
}