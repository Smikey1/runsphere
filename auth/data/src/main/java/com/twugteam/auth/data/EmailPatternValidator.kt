package com.twugteam.auth.data

import android.util.Patterns
import com.twugteam.auth.domain.PatternValidator

object EmailPatternValidator : PatternValidator {
    override fun matches(values: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(values).matches()
    }
}