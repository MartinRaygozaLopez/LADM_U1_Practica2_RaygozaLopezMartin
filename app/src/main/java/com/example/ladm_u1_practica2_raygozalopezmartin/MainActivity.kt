package com.example.ladm_u1_practica2_raygozalopezmartin

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }

        btnGuardar.setOnClickListener {
            if(rabInterna.isChecked() == false && rabSD.isChecked() == false){
                mensaje("Por favor seleccione donde se desea guardar el archivo")
                return@setOnClickListener
            }

            if(rabInterna.isChecked()){
                guardarArchivoInterno()
            } else {
                guardarArchivoSD()
            }
            editText2.setText("")
        }

        btnAbrir.setOnClickListener {
            if(rabInterna.isChecked()){
                leerArchivoInterno()
            } else {
                leerArchivoSD()
            }
        }
    }

    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var nombre = editText2.text.toString()
            var datosArchivo = File(rutaSD.absolutePath, nombre+".txt")
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = editText.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO! se creó el archivo")

            asignarTextos("")

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA")
            return
        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var nombre = editText2.text.toString()
            var datosArchivo = File(rutaSD.absolutePath, nombre+".txt")
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()
            var vector = data.split("&")

            asignarTextos(vector[0])

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun noSD():Boolean{

        var estado = Environment.getExternalStorageState() //Nos va a regresar si hay una menoria SD insertada en el telefono

        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }

        return false
    }

    private fun guardarArchivoInterno(){
        try{
            var nombre = editText2.text.toString()
            var flujoSalida = OutputStreamWriter(openFileOutput(nombre+".txt", Context.MODE_PRIVATE))
            var data = editText.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO! se creó el archivo")

            asignarTextos("")

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoInterno(){
        try{
            var nombre = editText2.text.toString()
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nombre+".txt")))

            var data = flujoEntrada.readLine()
            var vector = data.split("&")

            asignarTextos(vector[0])

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(m)
            .setPositiveButton("ACEPTAR") { d,i-> }
            .show()
    }

    fun asignarTextos(t1:String){
        editText.setText(t1)
    }


}
