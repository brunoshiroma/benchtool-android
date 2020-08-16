package com.brunoshiroma.benchtool_android.runner

import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FilenameFilter
import java.io.InputStreamReader
import java.math.BigInteger

interface BenchRunner{

    fun run(platform: String, type: String, iteration: Int, repeat: Int, expectedResult: String) : Triple<Int, BigInteger, String?>

}