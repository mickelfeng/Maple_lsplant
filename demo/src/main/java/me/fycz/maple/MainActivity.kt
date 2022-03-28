package me.fycz.maple

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.fycz.maple.databinding.ActivityMainBinding

/**
 * @author fengyue
 * @date 2022/3/28 21:15
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bridge: MapleBridge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.btTest.setOnClickListener {
            console(it)
            console(normal("1", 2, 3F))
        }

        binding.btHookBefore.setOnClickListener {
            console(it)
            bridge?.unhook()
            bridge = null
            bridge =
                MapleUtils.findAndHookMethod(
                    "me.fycz.maple.MainActivity",
                    this.classLoader,
                    "normal",
                    String::class.java,
                    Int::class.java,
                    Float::class.java,
                    object : MethodHook() {
                        override fun beforeHookedMethod(param: MapleBridge.MethodHookParam) {
                            param.args[0] = "Hook函数Before-----" + param.args[0]
                        }
                    }
                )
        }

        binding.btHookAfter.setOnClickListener {
            console(it)
            bridge?.unhook()
            bridge = null
            bridge =
                MapleUtils.findAndHookMethod(
                    "me.fycz.maple.MainActivity",
                    this.classLoader,
                    "normal",
                    String::class.java,
                    Int::class.java,
                    Float::class.java,
                    object : MethodHook() {
                        override fun afterHookedMethod(param: MapleBridge.MethodHookParam) {
                            console("Hook函数After-----" + param.args[0])
                        }
                    }
                )
        }

        binding.btHookReplace.setOnClickListener {
            console(it)
            bridge?.unhook()
            bridge = null
            bridge =
                MapleUtils.findAndHookMethod(
                    "me.fycz.maple.MainActivity",
                    this.classLoader,
                    "normal",
                    String::class.java,
                    Int::class.java,
                    Float::class.java,
                    object : MethodReplacement() {
                        override fun replaceHookedMethod(param: MapleBridge.MethodHookParam?): Any {
                            return "Hook函数replace-----"
                        }
                    }
                )
        }

        binding.btUnhook.setOnClickListener {
            console(it)
            bridge?.unhook()
            bridge = null
        }
    }

    private fun normal(a: String, b: Int, c: Float): String {
        return a + b + c
    }

    private fun console(v: View) {
        if (v is Button)
            binding.tvConsole.myAppend(v.text.toString())
    }

    private fun console(s: String) {
        binding.tvConsole.myAppend(s)
    }

    @SuppressLint("SetTextI18n")
    fun TextView.myAppend(s: String) {
        text = text.toString() + "\n" + s
    }
}