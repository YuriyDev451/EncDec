package com.qukunov.encdec

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.qukunov.encdec.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineStart
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    lateinit var ivValue: ByteArray
    lateinit var binding: ActivityMainBinding
    lateinit var cipherTextNew: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.encrypt.setOnClickListener {
            cipherTextNew = encrypt(applicationContext, binding.secureTxt.text.toString())
            binding.encryptTxt.text = cipherTextNew.toString()
        }

        binding.decrypt.setOnClickListener {
            decrypt(applicationContext, cipherTextNew)
        }


    }

    private fun encrypt(context: Context, strToEncrypt: String): ByteArray {
        val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
        val key = generateKey(binding.mediumKey.text.toString())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(plainText)
        ivValue = cipher.iv
        return cipherText
    }


    private fun decrypt(context: Context, dataToDecrypt: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        val key = generateKey(binding.medKey.text.toString())
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ivValue))
        val cipherText = cipher.doFinal(dataToDecrypt)
        buildString(cipherText, "decrypt")
        return cipherText
    }


    private fun generateKey(password: String): SecretKeySpec {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray()
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        val secretKeySpec = SecretKeySpec(key, "AES")
        return secretKeySpec
        Log.d("key", secretKeySpec.toString())
    }

//    private fun generateKey(password: String): String {
//        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
//        val bytes = password.toByteArray()
//        digest.update(bytes, 0, bytes.size)
//        val key = digest.digest()
//
//        // Преобразуем массив байтов ключа в строку Base64
//        val keyAsString = Base64.encodeToString(key, Base64.DEFAULT)
//
//        // Печатаем ключ для проверки
//        Log.d("Key", "Generated key (Base64): $keyAsString")
//
//        // Преобразуем обратно в SecretKeySpec, если нужно для дальнейшей работы
//        val secretKeySpec = SecretKeySpec(key, "AES")
//        Log.d("SecretKeySpec", secretKeySpec.toString())
//
//        return keyAsString // Возвращаем ключ в виде строки (опционально)
//    }

    private fun buildString(text: ByteArray, status: String): String{
        val sb = StringBuilder()
        for (char in text) {
            sb.append(char.toInt().toChar())
        }
        binding.finalTxt.text = sb.toString()
        return sb.toString()
    }
}