/*
PARA FAZER
CRIAR UMA NOVA LISTA COM USUARIOS
AJUSTAR TODOS OS METODOS, POIS IRÃO PRECISAR RECEBER ID DISCIPLINA E mIdUsuario
AJUSTAR A TELA DE LOGIN
 */


package br.com.registradorpresencaalunos.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import br.com.registradorpresencaalunos.R
import br.com.registradorpresencaalunos.repository.Dados
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_disciplinas.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class DisciplinasActivity: AppCompatActivity(), View.OnClickListener,AdapterView.OnItemSelectedListener {

    lateinit var mIdUsuario: String

    private val mDisciplinaModel: Dados = Dados()

    private lateinit var mDiaSemana: String
    private var mIdDisciplina: Int = 0

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplinas)

        mIdUsuario = intent.getStringExtra("idUsuario").toString()

        mDiaSemana = getDiaSemanaAtualStr()
        mIdDisciplina = mDisciplinaModel.getIdPeloDiaSemana(mDiaSemana, mIdUsuario)

        carregaListaDisciplinas()
        defineTextoTela(mIdDisciplina, mIdUsuario)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        spinner_disciplina.onItemSelectedListener = this
        button_registrar_presenca.setOnClickListener(this)
        button_desistir.setOnClickListener(this)
        text_todas_materias.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcess(0)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_registrar_presenca -> validaPresenca(mIdDisciplina)
            R.id.button_desistir -> finish()
            R.id.text_todas_materias -> carregaTodasDisciplinas("")
        }
    }

    //Funções spinner
    private fun carregaListaDisciplinas() {
        if (mDisciplinaModel.hasMateriaHoje(mDiaSemana, mIdUsuario)) {
            carregaDisciplinaDeHoje()
        } else {
            carregaTodasDisciplinas("Não há disciplinas hoje.")
        }
    }

    private fun carregaTodasDisciplinas(complemento: String) {
        text_todas_materias.visibility = View.GONE

        toast("$complemento Todas as matérias selecionadas!")
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,mDisciplinaModel.getTodasMaterias(mIdUsuario))
        spinner_disciplina.adapter = adapter
    }

    private fun carregaDisciplinaDeHoje() {
        toast("Matéria de hoje selecionada!")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            mDisciplinaModel.getMateriaPeloDiaSemana(mDiaSemana, mIdUsuario)
        )
        spinner_disciplina.adapter = adapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        toast("Selecione uma opção.")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_disciplina -> {
                val nomeMateriaSelecionada = parent?.getItemAtPosition(position)
                mIdDisciplina = mDisciplinaModel.getIdPelaMateria(nomeMateriaSelecionada.toString(), mIdUsuario)
                defineTextoTela(mIdDisciplina,mIdUsuario)
            }
        }
    }

    //Funções manipula texto na tela
    private fun defineTextoTela(idDisciplina: Int, mIdUsuario: String) {
        image_circle7.setColorFilter(ActivityCompat.getColor(this,R.color.ic_tint_verde))
        text_materia_hoje.text = SimpleDateFormat("EEEE, dd/MM/yyyy").format(Date())

        text_materia_dia_semana.text = "Dia da semana: " + mDisciplinaModel.getDiaSemana(idDisciplina,mIdUsuario)
        text_data_hora_aula_inicio.text = "Inicio aula: " + mDisciplinaModel.getInicioAula(idDisciplina,mIdUsuario)
        text_data_hora_aula_fim.text = "Fim aula: " + mDisciplinaModel.getFimAula(idDisciplina,mIdUsuario)
        text_materia.text = "Materia: " + mDisciplinaModel.getMateria(idDisciplina,mIdUsuario)
        text_professor.text = "Professor(a): " + mDisciplinaModel.getProfessor(idDisciplina,mIdUsuario)
        text_andar_sala.text = "Local: " + mDisciplinaModel.getLocalAula(idDisciplina,mIdUsuario)
        text_presenca_registrada.text = "Presença registrada? " + mDisciplinaModel.getPresencaRegistrada(idDisciplina,mIdUsuario)
        text_local_presenca.text = "Local do registro: " + mDisciplinaModel.getLocalRegistro(idDisciplina,mIdUsuario)
        if((mDisciplinaModel.getPresencaRegistrada(idDisciplina,mIdUsuario) == "NÃO") && (mDisciplinaModel.getLocalRegistro(idDisciplina,mIdUsuario) != "")){
            image_circle7.setColorFilter(ActivityCompat.getColor(this,R.color.ic_tint_alerta))
        }
    }

    private fun defineTextoPresenca(id: Int, mIdUsuario: String){
        if (mDisciplinaModel.setPresencaRegistrada(id,mIdUsuario) == true) {
            text_presenca_registrada.text = "Presença registrada? SIM"
            toast("Presença registrada!")
        }
    }

    //Validação
    private fun validaPresenca(id: Int) {
        when (mGetDiaSemana() == getDiaSemanaAtualStr() && getHoraAtual() > mGetInicioAula() && getHoraAtual() < mGetFimAula()) {
            true -> {
                if (getUltimaLocalização()) {
                    validaLocalPresenca(id)
                }
            }
            false -> {
                if (mGetDiaSemana() != "EAD") {
                    toast("Você está fora do horário da aula!")
                } else {
                    toast("EAD não é necessário registrar presença!")
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun validaLocalPresenca(id: Int): Boolean {
        val mLocalizacaoFaculdade = localizacaoFaculdade()
        var retorno = false
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            var localizacao: Location? = task.result
            if (localizacao == null) {
                requestNovosDadosDeLocalizacao()
            } else {
                var latAtual = localizacao.latitude.toString()
                var longAtual = localizacao.longitude.toString()
                if ((mLocalizacaoFaculdade.latitude == latAtual) && (mLocalizacaoFaculdade.longitude == longAtual)) {
                    defineTextoPresenca(id, mIdUsuario)
                    image_circle7.setColorFilter(ActivityCompat.getColor(this,R.color.ic_tint_verde))
                    mDisciplinaModel.setLocalRegistro(id, mIdUsuario, "$latAtual, $longAtual")
                    text_local_presenca.text = "Local do registro: " + mDisciplinaModel.getLocalRegistro(id, mIdUsuario)
                    retorno = true
                } else {
                    toast("Fora da faculdade. Para registrar presença é necessário estar na localizaçao: ${mLocalizacaoFaculdade.latitude}, ${mLocalizacaoFaculdade.longitude}")
                    image_circle7.setColorFilter(ActivityCompat.getColor(this,R.color.ic_tint_alerta))
                    mDisciplinaModel.setLocalRegistro(id, mIdUsuario, "$latAtual, $longAtual")
                    text_local_presenca.text = "Local do registro: " + mDisciplinaModel.getLocalRegistro(id, mIdUsuario)
                    retorno = false
                }
            }
        }
        return retorno
    }

    //Localização
    @SuppressLint("MissingPermission")
    private fun getUltimaLocalização(): Boolean {
        var permissoesOk = false
        if (verificarPermissoes()) {
            if (isLocalizacaoAtivada()) {
                permissoesOk = true
            } else {
                toast("Ative a localização")
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissoes()
        }
        return permissoesOk
    }

    @SuppressLint("MissingPermission")
    private fun requestNovosDadosDeLocalizacao() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocalizacaoCallback,
            Looper.myLooper()
        )
    }

    private val mLocalizacaoCallback = object : LocationCallback() {
        override fun onLocationResult(resultadoLocalization: LocationResult) {
            var mUltimaLocalizacao: Location = resultadoLocalization.lastLocation
            text_local_presenca.text =
                "Local do registro: ${mUltimaLocalizacao.latitude}, ${mUltimaLocalizacao.longitude}"
        }
    }

    private fun isLocalizacaoAtivada(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun verificarPermissoes(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermissoes() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestPermissao: Int,permissoes: Array<String>,grantResultados: IntArray) {
        if (requestPermissao == PERMISSION_ID) {
            if ((grantResultados.isNotEmpty() && grantResultados[0] == PackageManager.PERMISSION_GRANTED)) {
                getUltimaLocalização()
            }
        }
    }

    //Auxiliares
    private fun getHoraAtual(): Date {
        val formataDataHoraAtual = SimpleDateFormat("HH:mm:ss")
        val dataAtual: String = formataDataHoraAtual.format(Date())

        return formataHora(dataAtual)
    }

    private fun getDiaSemanaAtualStr(): String {
        val formataDataHoraAtual = SimpleDateFormat("EEEE")
        val dataAtual: String = formataDataHoraAtual.format(Date())

        return dataAtual
    }

    private fun mGetInicioAula(): Date = formataHora(mDisciplinaModel.getInicioAula(mIdDisciplina,mIdUsuario))

    private fun mGetFimAula(): Date = formataHora(mDisciplinaModel.getFimAula(mIdDisciplina,mIdUsuario))

    private fun mGetDiaSemana(): String = mDisciplinaModel.getDiaSemana(mIdDisciplina,mIdUsuario)

    private fun formataHora(dataString: String): Date =
        SimpleDateFormat("HH:mm:ss").parse(dataString)

    class localizacaoFaculdade {
////        Faculdade
//        val latitude: String = "-23.536286105990403"
//        val longitude: String = "-46.560337171952156"

        val latitude: String = "-23.5851854"
        val longitude: String = "-46.5190304"

    }

    private fun toast(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
    }
}