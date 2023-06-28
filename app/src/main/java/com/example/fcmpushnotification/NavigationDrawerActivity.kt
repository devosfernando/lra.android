package com.example.fcmpushnotification


import android.content.pm.PackageManager
import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ekn.gruzer.gaugelibrary.HalfGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.example.fcmpushnotification.databinding.ActivityNavigationDrawerBinding
import com.example.fcmpushnotification.dto.dataResponse
import com.example.fcmpushnotification.dto.dataResponseYear
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class NavigationDrawerActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding
    private var urlBase = "http://devosfernando.com:38900/api/1"
    private val CODIGO_PERMISOS_ALMACENAMIENTO = 2


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)

        binding.appBarNavigationDrawer.fab.setOnClickListener {
            /*Snackbar.make(view, "Replace with your own action juan", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            Toast.makeText(baseContext,"Actualizando datos de la grafica",Toast.LENGTH_LONG).show()
            getAccesToken()
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_slideshow, R.id.nav_logout,R.id.nav_configuration
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /**
         * ACCEDE A LOS PERMISOS DE ESCRITURA AL ALMACENAMIENTO
         * */
        writeLogToFile()

        /**
         * EJECUCIÓN DE METODO
         * **/
        getAccesToken()



    }

    /**
     * CREA LA GRAFICA DE TIPO AGUJA EN LA VIEW MODEL "REPORTES" UTILIZANDO LA LIBRERIA HALFGAUGE
     * */
    private fun onCreateChartKpi(kpiPrevMonht: String?) {
        try {
            /**
             * CREAR LA FUNCIONALIDAD PARA KPI
             *
             * ***/

            val ranges = Range()
            val ranges1 = Range()
            val ranges2 = Range()
            //setContentView(R.layout.report)
            //IdBtnUp = findViewById(R.id.IdBtnUp)
            //IdBtnDown = findViewById(R.id.IdBtnDown)
            val halfGauge: HalfGauge = findViewById(R.id.IdReport)


            ranges.color = Color.parseColor("#DA3851")
            ranges.from = 0.0
            ranges.to = 21.5

            ranges1.color = Color.parseColor("#F8CD51")
            ranges1.from = 21.5
            ranges1.to = 43.0

            ranges2.color = Color.parseColor("#48AE64")
            ranges2.from = 43.0
            ranges2.to = 100.0

            halfGauge.minValue = 0.0
            halfGauge.maxValue = 100.0
            halfGauge.value = 0.0


            halfGauge.addRange(ranges)
            halfGauge.addRange(ranges1)
            halfGauge.addRange(ranges2)

            /**
             * SE COLOCAN VARIAS EXEPCIONES PARA CONTROLAR EL ERROR TIPIFICADO, ESTO BASADO EN LOS ERRORES EN LA EJECUCIÓN DE
             * ANDROID 13 POR EL VALOR AL MOMENTO DE REALIZAR EL METODO TODOUBLE
             * */

            try {
                if (!kpiPrevMonht.isNullOrEmpty()){
                    val df = DecimalFormat("#.##")
                    df.maximumFractionDigits = 2
                    val valorDecimal  = (df.format(kpiPrevMonht.toDouble()).toString()).replace(",",".")
                    Log.d("onCreateChartKpi_TRYCAT","ESTE ES EL NUMERO DECIMAL ${valorDecimal}")
                    halfGauge.value = valorDecimal.toDouble()
                }else{
                    halfGauge.value = 100.0
                }
            }catch (e: NullPointerException) {
                halfGauge.value = 50.0
                Log.d("onCreateChartKpi_TRYCAT","NULL POINTER EXEPCION")
                Log.d("onCreateChartKpi_TRYCAT",e.message.toString())
                messageToast()
            } catch (e: IllegalStateException) {
                halfGauge.value = 50.0
                Log.d("onCreateChartKpi_TRYCAT","IllegalStateException")
                Log.d("onCreateChartKpi_TRYCAT",e.message.toString())
                messageToast()
            } catch (e: IllegalArgumentException) {
                halfGauge.value = 50.0
                Log.d("onCreateChartKpi_TRYCAT","legalArgumentException")
                Log.d("onCreateChartKpi_TRYCAT",e.message.toString())
                messageToast()
            } catch (e: IndexOutOfBoundsException) {
                halfGauge.value = 50.0
                Log.d("onCreateChartKpi_TRYCAT","IndexOutOfBoundsException")
                Log.d("onCreateChartKpi_TRYCAT",e.message.toString())
                messageToast()
            }
            setContentView(binding.root)
            Log.d("onCreateChartKpi_TRYCAT","ERORR EN SE VA POR TRY")
        }catch (e:Exception){
            Log.d("onCreateChartKpi_TRYCAT","ERORR EN SE VA POR CATCH")
            Log.d("onCreateChartKpi_TRYCAT",e.message.toString())
            leerLogCat()
        }

    }

    private fun messageToast(){
        return Toast.makeText(
            baseContext,
            "Oops. Hay un error al pintar la aguja !",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * CREA EN EL VIEW MODEL DE LA APLICACIÓN "REPORTES" UNA DE LAS GRAFICAS, KPI ANUAL.
     * SE UTILIZA LA LIBRERIA DE MPANDROIDCHART PARA DIBUJAR SOBRE UN PLANO CARTESIANO DIVIDO EN LA ABSCISAS (X) Y COORDENADAS (Y),
     * SE DIBUJAN DOS DATASET SOBRE EL PLANO, UNO CORRESPONDIENTE AL KPI ESTIMADO PARA EL FINAL DEL MES Y OTRO KPI ESTABLECIDO PARA TODOS LOS
     * MESES DEL AÑO
     * */
    private fun onCreateChartKpiYear(kpiYear: dataResponseYear?){

        try {
            // Obtén una referencia a la vista de gráfica
            val chartView = findViewById<LineChart>(R.id.chart)
            val entries = ArrayList<Entry>()
            val months = listOf("","Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

            var sizeYear = (kpiYear?.response?.size.toString()).toInt()
            for (i in 1..sizeYear){
                if (!((kpiYear?.response?.get(i-1)?.kpireal.toString()) == "0")){
                    entries.add(Entry( ("${i}f").toFloat(), "${(kpiYear?.response?.get(i-1)?.kpireal.toString())}f".toFloat()))
                }
            }

            //CONFIGURACIÓN PARA FORMATEAR LOS DOS DECIMALES
            val yAxis = chartView.axisLeft
            val yAxisFormat = DecimalFormat("###.###") // aquí puedes especificar el número de decimales que deseas mostrar
            yAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return yAxisFormat.format(value)
                }
            }

            //CONFIGURACIÓN PARA MOSTAR MESES
            val xAxis = chartView.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(months)


            // Crea el conjunto de datos para la primera línea
            val dataSet1 = LineDataSet(
                entries,
                "Kpi Real"
            )
            dataSet1.color = Color.parseColor("#277A3E")
            dataSet1.valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return yAxisFormat.format(entry?.y)
                }
            }

            // Crea el conjunto de datos para la segunda línea
            val dataSet2 = LineDataSet(
                listOf(
                    Entry(1f, (kpiYear?.response?.get(0)?.kpiestimado.toString()).toFloat()),
                    Entry(2f, (kpiYear?.response?.get(1)?.kpiestimado.toString()).toFloat()),
                    Entry(3f, (kpiYear?.response?.get(2)?.kpiestimado.toString()).toFloat()),
                    Entry(4f, (kpiYear?.response?.get(3)?.kpiestimado.toString()).toFloat()),
                    Entry(5f, (kpiYear?.response?.get(4)?.kpiestimado.toString()).toFloat()),
                    Entry(6f, (kpiYear?.response?.get(5)?.kpiestimado.toString()).toFloat()),
                    Entry(7f, (kpiYear?.response?.get(6)?.kpiestimado.toString()).toFloat()),
                    Entry(8f, (kpiYear?.response?.get(7)?.kpiestimado.toString()).toFloat()),
                    Entry(9f, (kpiYear?.response?.get(8)?.kpiestimado.toString()).toFloat()),
                    Entry(10f, (kpiYear?.response?.get(9)?.kpiestimado.toString()).toFloat()),
                    Entry(11f, (kpiYear?.response?.get(10)?.kpiestimado.toString()).toFloat()),
                    Entry(12f, (kpiYear?.response?.get(11)?.kpiestimado.toString()).toFloat()),
                ),
                "Kpi Estimado"
            )
            dataSet2.color = Color.BLUE
            dataSet2.valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return yAxisFormat.format(entry?.y)
                }
            }

            // Crea un objeto de datos de la gráfica y agrega ambos conjuntos de datos
            val chartData = LineData(dataSet1, dataSet2)

            //Formateador
            //val formatoValores = DefaultValueFormatter(2)


            // Configura la gráfica
            chartView.data = chartData
            //chartView.axisLeft.valueFormatter = formatoValores
            chartView.animateX(1000)
            chartView.axisRight.isEnabled = false
            chartView.description.isEnabled = false
            chartView.legend.isEnabled = true
            chartView.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            chartView.description.text = "Meses del año"
            chartView.description.textColor = Color.BLACK
            chartView.description.textSize = 14f
            Log.d("onCreateChartKpiYear_TR","ERORR EN SE VA POR TRY")
        }catch (e:Exception){
            Log.d("onCreateChartKpiYear_TR","ERORR EN SE VA POR CATCH")
            Log.d("onCreateChartKpiYear_TR",e.message.toString())
            leerLogCat()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    /**
     * SE INSTANCIAS DOS REQUEST DE LA LIBRERIA Y SE MANDA EN LA CABECERA DE LA PETICIÓN EL TOKEN PARA DEVOVLER DATOS, ESTO SE GENERA A
     * DOS SERVICIOS, SE MANDA EN LA CABECERA DE LA PETICIÓN EL TOKEN:
     * 1. QUE OBTIENE EL KPI AL AÑO
     * 2. QUE OBTIENE EL KPI  ACTUAL
     * */
    private fun readWebService(token: String) {
        try {
            val queue = Volley.newRequestQueue(this)
            val stringRequestKpiLast = object : StringRequest(Method.GET, "${urlBase}/kpi/prevMonht/kpi",
                Response.Listener { response ->
                    Log.d("RESPONSE FECHA ACTUAL",response)
                    paint(response,"lastMonht")
                },
                Response.ErrorListener { response ->
                    // Show timeout error message
                    Toast.makeText(
                        baseContext,
                        "Oops. Timeout error Kpi Last!",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = token
                    return headers
                }
            }
            val stringRequestKpiYear = object : StringRequest(Method.GET, "${urlBase}/kpi/year",
                Response.Listener { response ->
                    Log.d("RESPONSE KPI YEAR:",response)
                    paint(response,"year")
                },
                Response.ErrorListener { response ->
                    // Show timeout error message
                    Toast.makeText(
                        baseContext,
                        "Oops. Timeout error Kpi Year!",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = token
                    return headers
                }
            }
            stringRequestKpiLast.retryPolicy = DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            stringRequestKpiYear.retryPolicy = DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            //queue.add(stringRequest)
            queue.add(stringRequestKpiLast)
            queue.add(stringRequestKpiYear)
            Log.d("readWebService_TRYCATCH","ERORR EN SE VA POR TRY")
        }catch(e: Exception){
            Log.d("readWebService_TRYCATCH","ERORR EN SE VA POR CATCH")
            Log.d("readWebService_TRYCATCH",e.message.toString())
            leerLogCat()
        }



    }

    /**
     *OBTENER TOKEN DE AUTENTICACIÓN PARA LLAMAR AL METODO READWEBSERVICES PASANDOLE EL TOKEN VIGENTE POR 10 MINUTOS
     * */
    fun getAccesToken() {
        try {
            val bundle = intent.extras
            val emailUser = bundle?.getString("email")
            if (!emailUser.isNullOrEmpty()) {
                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                val hView  = navigationView.getHeaderView(0)
                (hView.findViewById(R.id.textEmail) as TextView).text = emailUser
            }
            val urlPost = "${urlBase}/auth/securityToken"
            val params = HashMap<String, String>()
            params["email"] = emailUser.toString()
            val jsonObject = JSONObject(params as Map<*, *>)
            val queue = Volley.newRequestQueue(this)
            val stringRequestOne = JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                urlPost,
                jsonObject,
                { response ->
                    if(response["token"].toString().isNotEmpty()){
                        readWebService(response["token"].toString())
                    }else{
                        Toast.makeText(baseContext,"Oops, not access token",Toast.LENGTH_SHORT).show()
                    }
                },
                { response ->

                    Toast.makeText(baseContext,"Oops, backend not responding ",Toast.LENGTH_SHORT).show()
                })
            stringRequestOne.retryPolicy = DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            queue.add(stringRequestOne)
            Log.d("getAccesToken_TRYCATCH","ERORR EN SE VA POR TRY")
        }catch(e: Exception){
            Log.d("getAccesToken_TRYCATCH","ERORR EN SE VA POR CATCH")
            Log.d("getAccesToken_TRYCATCH",e.message.toString())
            leerLogCat()
        }
    }
    /**
     * FUNCIÓN QUE SERIALIZA EL MENSAJE PARA PASAR LA INFORMACIÓN EN DOS METODOS
     * onCreateChartKpi()
     * onCreateChartKpiYear()
     * */
    fun paint(data: String,endPoint: String){
        val gson = Gson()
        var kpiReal = ""
        var dataResponseYearOne: dataResponseYear? = null
        try {
            if (endPoint == "lastMonht"){
                //SERIALIZAR
                val dataResponse = gson.fromJson(data, dataResponse::class.java)
                kpiReal = (dataResponse.response?.get(0)?.hist_kpiReal.toString())
                val dateFormat = dataResponse.response?.get(0)?.hist_date?.substring(0,10)
                var textView = findViewById<TextView>(R.id.IdTextView)
                textView.setText("Kpi fecha actual: ${dateFormat} ")
            }else{
                //SERIALIZAR A CLASE MESSAGE YEAR
                dataResponseYearOne  = gson.fromJson(data,dataResponseYear::class.java)
            }
            if (kpiReal != null) {
                when (endPoint) {
                    "lastMonht" -> onCreateChartKpi(((kpiReal.toDouble() * 100.0).toString()))
                    else -> { // Note the block
                        Log.d("Response case ", "no corresponde a ninguno de los datos del case")
                    }
                }
            }
            if (dataResponseYearOne != null){
                when (endPoint) {
                    "year" -> onCreateChartKpiYear(dataResponseYearOne)
                    else -> { // Note the block
                        Log.d("Response case ", "no corresponde a ninguno de los datos del case")
                    }
                }
            }
            Log.d("paint_TRYCATCH","ERORR EN SE VA POR TRY")
        }catch (e: Exception){
            Log.d("paint_TRYCATCH","ERORR EN SE VA POR CATCH")
            Log.d("paint_TRYCATCH",e.message.toString())
            leerLogCat()
        }
    }


    /**
     * PREGUNTA SI NO TIENE PERMISOS AL 'MANAGE_EXTERNAL_STORAGE' ENTRA Y SE AGREGAN CON REQUESTPERMISSIONS
     * */
    fun writeLogToFile() {

        if (ContextCompat.checkSelfPermission(this, MANAGE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso de almacenamiento externo
            ActivityCompat.requestPermissions(this, arrayOf(MANAGE_EXTERNAL_STORAGE), CODIGO_PERMISOS_ALMACENAMIENTO)
            Log.d("PERMISOS","El permiso para el almacenamiento está concedido")
        }else{
            // Hay permisos en almacenamiento externo
            Log.d("FALSE PERMISOS","ERORR EN PERMISOS")
        }


    }

    /**
     * FUNCIÓN PARA ESCRIBIR UN LOG EN LA RUTA DOWNLOADS, SE LLAMA A LA FUNCIÓN PASANDO EL MENSAJE QUE
     * DESEA QUE SE ESCRIBA, ACTUALMENTE NO SE ESTA LLAMANDO DESDE OTRO METODO PORQUE SE ACTIVO OTRO LOG
     * QUE TOMA LA EJECUCIÓN DEL LOGCAT DIRECTO DE LA APK EN RUNTIME
     * */
     fun permisoDeAlmacenamientoConcedido(log:String) {

        //Se guarda en una variable el formato de la fecha actual
         val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        //Se guarda nombre del txt que se quiere generar
         val fileName = "Log_${timeStamp}_BBVA.txt"
        //Se guarda la dirección donde quedara el archivo en la carpeta "Logs"
         val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Logs");
        //Se valida si el directorio existe de lo contrario lo crea
         if (!dir.exists()) {
             dir.mkdirs()
         }
        //Se guarda la dirección donde quedara el txt
         val file = File(dir, fileName)
        //Se agrega la salida mediante el metodo
         val fos = FileOutputStream(file, true)
        //Se hace un salto de linea por cada ejecución que sea llamado
         val logWithNewLine = "$log\n"
        //Se escribe en un ByteArray
         fos.write(logWithNewLine.toByteArray())
         fos.flush()
         fos.close()


    }

    /**
     * FUNCION QUE GUARDA UN LOG , LOG EN EL QUE SE GUARDARA EL LOGCAT EN LAS DESCARGAS DEL DIRECTORIO
     * */
     fun leerLogCat(){
         val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
         val fileName = "Log_${timeStamp}_BBVA_logcat.txt"
         val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Logs");
         if (!dir.exists()) {
             dir.mkdirs()
         }
        /**
         *COMANDO QUE SE EJECUTA EN EL OS PARA HABILITAR EL LOGCAT HERRMIENTA DE QUE MANEJA LOS MENSAJES DE DEPURACIÓN
         * */
         val logcatProcess = Runtime.getRuntime().exec("logcat -d")
         val logcatBufferedReader = BufferedReader(InputStreamReader(logcatProcess.inputStream))
         val logcatStringBuilder = StringBuilder()

         var line: String?
         while (logcatBufferedReader.readLine().also { line = it } != null) {
             logcatStringBuilder.append(line)
             logcatStringBuilder.append('\n')
         }

         File(dir,fileName).writeText(logcatStringBuilder.toString())

     }

}











