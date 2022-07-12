package com.perpetio.well_be.utils

import com.perpetio.well_be.R

enum class SignEnum(val key: Int) {
    NONE(0),
    BLOT(1),
    CLOUD(2),
    FLOWERS(3),
    HEART(4),
    SPOT(5),
    STARS(6)
}

object Sign {
    fun getSignDrawableByKey(key: Int): Int? {
        when (key) {
            SignEnum.BLOT.key ->
                return R.drawable.ic_blot_dark_blue
            SignEnum.CLOUD.key ->
                return R.drawable.ic_cloud_dark_blue
            SignEnum.FLOWERS.key ->
                return R.drawable.ic_flowers_pink
            SignEnum.HEART.key ->
                return R.drawable.ic_heart_pink
            SignEnum.SPOT.key ->
                return R.drawable.ic_spot_pink
            SignEnum.STARS.key ->
                return R.drawable.ic_stars_yellow
        }
        return null
    }

    fun getSignKeyByChipId(id: Int?): Int {
        when (id) {
            null, R.id.chip_none -> {
                return SignEnum.NONE.key
            }
            R.id.chip_blot -> {
                return SignEnum.BLOT.key
            }
            R.id.chip_cloud -> {
                return SignEnum.CLOUD.key
            }
            R.id.chip_flowers -> {
                return SignEnum.FLOWERS.key
            }
            R.id.chip_heart -> {
                return SignEnum.HEART.key
            }
            R.id.chip_spot -> {
                return SignEnum.SPOT.key
            }
            R.id.chip_stars -> {
                return SignEnum.STARS.key
            }
        }
        return SignEnum.NONE.key
    }
}