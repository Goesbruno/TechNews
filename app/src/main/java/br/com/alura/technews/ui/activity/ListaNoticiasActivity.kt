package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment


private const val TITULO_APPBAR = "Notícias"


class ListaNoticiasActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_noticias)
        title = TITULO_APPBAR
        configuraFragment()
        configuraListenerDoFragment()
    }

    private fun configuraListenerDoFragment() {
        val listener = FragmentOnAttachListener { fragmentManager, fragment ->
            if (fragment is ListaNoticiasFragment) {
                fragment.quandoNoticiaSeleciona = {
                    abreVisualizadorNoticia(it)
                }
                fragment.quandoFabSalvaNoticiaClicado = {
                    abreFormularioModoCriacao()
                }
            }
        }
        fragmentManager.addFragmentOnAttachListener(listener)
    }

    private fun configuraFragment() {
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.activity_lista_noticias_fragment, ListaNoticiasFragment())
        fragmentTrasaction.commit()
    }


    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(it: Noticia) {
        val intent = Intent(this, VisualizaNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, it.id)
        startActivity(intent)
    }

}
