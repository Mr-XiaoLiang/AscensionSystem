package com.lollipop.ascensionsystem.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ascensionsystem.R

/**
 * @author lollipop
 * @date 2020/3/15 15:11
 * 基础的Activity
 */
@SuppressLint("Registered")
abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

}