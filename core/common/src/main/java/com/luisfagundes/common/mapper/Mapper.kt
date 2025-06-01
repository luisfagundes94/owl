package com.luisfagundes.common.mapper

interface Mapper<S, T> {
    fun map(source: S): T
}
