package com.goodmorningvoca.std

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import com.goodmorningvoca.std.model.Data
import com.goodmorningvoca.std.model.GlobalVariable
import com.goodmorningvoca.std.model.LoginResult
import com.brian.mytube.R
import com.google.gson.GsonBuilder
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream

class SplashActivity : AppCompatActivity() {
    val INI_FILE ="/gmv.ini"
    val TAG = "SPLASH"
    val client = OkHttpClient()
    private var mDelayHandler : Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var needUpdate = false
        getVerfromServer()  //  서버의 정보를 Globalvariables.ver 에 저장한다.
        //checkver()
        val token = getTokenFromFile()
        Log.d(TAG ,"token $token" )
        GlobalVariable.token = token?.trim()

    }


    //region Version Check




    private fun getTokenFromFile() : String? {
        var inputString : String? = null
        try {
            val inputStream: InputStream = File(getApplicationInfo().dataDir + INI_FILE).inputStream()
            inputString = inputStream.bufferedReader().use { it.readText() }
        }catch (e : Exception){return null }

        return inputString
    }


    fun getVerfromServer() :Boolean{
        val url = Data.URL_DOMAIN  + "/api/ver"
        val json : JSONObject = JSONObject()

        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON , json.toString())
        val request = Request.Builder()
                //.addHeader("Authorization","Bearer "+ GlobalVariable.token )
                .url(url)
                .build()
        try {
            //val response  = client.newCall(request ).execute()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    Log.e( TAG ,"Failed to Login AutoLogin ")
                    goToLoginActivity()
                }
                override fun onResponse(call: Call?, response: Response?) {

                    val body = response?.body()?.string()?.trim()
                    try {
                        GlobalVariable.ver = body!!.toInt()
                    }catch (e:NumberFormatException)
                    {
                        GlobalVariable.ver = 0
                    }
                    checkver()
                }
            })





            return true
        }catch ( e:Exception){   /// 로그인 실패하면 token이 없어서 에러 난다.
            return false
        }
    }

    fun checkver(){
        try {
            val pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            val version  = pInfo.versionCode ;
            if(GlobalVariable.ver > version   ){
                runOnUiThread {
                    onUpdateNeeded("https://play.google.com/store/apps/details?id=com.goodmorningvoca.std.app")
                }
            }else{
                autoLogin()
            }

        }catch( e : PackageManager.NameNotFoundException ) {
            //Log.d("MyApp", "PackageManager Catch : "+e.toString());
            // 그냥 넘어 간다.
        }

    }
    fun  onUpdateNeeded(updateUrl:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("새로운 버전이 있습니다.")
        builder.setMessage("새로운 버전으로 업데이트 바랍니다. ")
        builder.setPositiveButton("업데이트"){ dialog ,
                                           which ->
            redirectStore(updateUrl)
        }
        builder.setNegativeButton("다음에"){dialog,which->
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun redirectStore(updateUrl: String?){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    public fun goToMainActivity(){
        val intent = Intent( this  ,  MainActivity::class.java )
        startActivity(intent)
        finish()
    }
    public fun goToLoginActivity(){
        val intent = Intent(this , LoginActivity::class.java)
        startActivity(intent )
        finish()
    }
    /// 로컬에 저장된 Token을 가지고 정보를 자동 로그인을 한다.
    private fun autoLogin()  {

        // 성공하면 MainActivity , 실패하면 LoginActivity

        if( GlobalVariable.token.isNullOrEmpty() )
        {
            goToLoginActivity()
            return
        }

        val url = Data.URL_DOMAIN  + "/api/login_check"
        val json : JSONObject  = JSONObject()
        var loginOk : Boolean = false


        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON , json.toString())
        val request = Request.Builder()
                .addHeader("Authorization","Bearer "+ GlobalVariable.token )
                .url(url)
                .build()



        //Data.logged_info = loginResult

        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    Log.e( TAG ,"Failed to Login AutoLogin ")
                    goToLoginActivity()
                }
                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()

                    try {
                        val loginResult = gson.fromJson(body, LoginResult::class.java)
                        //GlobalVariable.token = loginResult.token.token
                        GlobalVariable.user = loginResult.user
                        GlobalVariable.school_name = loginResult.school_name
                    }catch (e:Exception){
                        goToLoginActivity()
                    }
                    loginOk = true
                    Log.e(TAG , "AUTO LOGIN OK ")

                    goToMainActivity()
                }
            })

        }catch ( e:Exception){   /// 로그인 실패하면 token이 없어서 에러 난다.
            goToLoginActivity()
            //return false
        }

    }
    //endregion


}
