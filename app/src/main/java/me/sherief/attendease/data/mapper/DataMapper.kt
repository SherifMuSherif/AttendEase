package me.sherief.attendease.data.mapper

interface DataMapper<C, D> {
    fun mapToDomain(cache: C): D
    fun mapFromDomain(domain: D): C
}