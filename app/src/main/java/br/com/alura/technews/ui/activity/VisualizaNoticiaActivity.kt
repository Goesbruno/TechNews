package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import br.com.alura.technews.R
import br.com.alura.technews.databinding.ActivityVisualizaNoticiaBinding
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment


private const val TITULO_APPBAR = "Notícia"

private lateinit var binding: ActivityVisualizaNoticiaBinding

class VisualizaNoticiaActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    private val noticiaId: Long by lazy {
        intent.getLongExtra(NOTICIA_ID_CHAVE, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizaNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = TITULO_APPBAR
        configuraFragment()
        configuraListenerFragment()
    }

    private fun configuraListenerFragment() {
        val listener = FragmentOnAttachListener { fragmentManager, fragment ->
            if (fragment is VisualizaNoticiaFragment) {
                fragment.quandoFinalizaTela = { finish() }
                fragment.quandoSelecionaMenuEdicao = { abreFormularioEdicao() }
            }
        }
        fragmentManager.addFragmentOnAttachListener(listener)
    }

    private fun configuraFragment() {
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(
            R.id.activity_visualiza_noticia_fragment,
            VisualizaNoticiaFragment()
        )
        fragmentTrasaction.commit()
    }


    private fun abreFormularioEdicao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticiaId)
        startActivity(intent)
    }

}
