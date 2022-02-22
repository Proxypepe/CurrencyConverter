package com.example.currencyconverter.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "currencies")
data class CurrencyDTO (
    @PrimaryKey
    @NotNull
    val name: String,
    @NotNull
    val price: Float
)