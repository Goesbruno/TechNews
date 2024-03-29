package br.com.alura.technews.ui.activity.extensions

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

fun Activity.mostraErro(mensagem: String) {
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}

//Função genérica feita para simplificar as transações de fragments
fun AppCompatActivity.transacaoFragment(executa: FragmentTransaction.() -> Unit) {
    val transacao = supportFragmentManager.beginTransaction()
    executa(transacao)
    transacao.commit()
}