package br.com.registradorpresencaalunos.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import br.com.registradorpresencaalunos.R
import br.com.registradorpresencaalunos.repository.Dados
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mUsuarioModel: Dados = Dados()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_logar.setOnClickListener(this)
        button_sair.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_logar -> loginValidacao()
            R.id.button_sair -> finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcess(0)
    }

    private fun loginValidacao() {
        var validacaoCampos: Boolean = validaEntradas(edit_rgm.text.isEmpty(), edit_senha.text.isEmpty())
        if (validacaoCampos) {
            var validaCredenciais: Boolean = validaCredenciais(edit_rgm.text.toString(), edit_senha.text.toString())
            if(validaCredenciais){
                acessaDisciplina()
            }
        }
    }

    private fun acessaDisciplina() {
        val intent: Intent = Intent(this, DisciplinasActivity::class.java);
        intent.putExtra("idUsuario",edit_rgm.text.toString())
        startActivity(intent);
    }

    private fun toast(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
    }

    private fun validaEntradas(play1: Boolean, play2: Boolean): Boolean {
        if (play1 && play2) {
            toast("Preencher todos os campos.")
            ic_play.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_alerta))
            ic_play2.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_alerta))
            return false
        } else if (play1 && !play2) {
            toast("Preencher RGM")
            ic_play.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_alerta))
            ic_play2.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_verde))
            return false
        } else if (!play1 && play2) {
            toast("Preencher Senha")
            ic_play.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_verde))
            ic_play2.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_alerta))
            return false
        } else {
            ic_play.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_verde))
            ic_play2.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_verde))
            return true
        }
    }

    private fun validaCredenciais(usuario: String, senha: String): Boolean {
        if(mUsuarioModel.validaLoginSenha(usuario, senha)){
            return true
        }else{
            toast("Usuário ou senha incorreto.")
            ic_play.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_alerta))
            ic_play2.setColorFilter(ActivityCompat.getColor(this, R.color.ic_tint_alerta))
            return false
        }
    }
}