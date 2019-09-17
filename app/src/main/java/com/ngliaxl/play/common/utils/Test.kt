package com.ngliaxl.play.common.utils

class Test {

    companion object InnerClass {
        val a = 10
        fun b() {
            println("b")
        }
    }

    fun c(){
        Test.b()
        val c = Test.a
    }

}