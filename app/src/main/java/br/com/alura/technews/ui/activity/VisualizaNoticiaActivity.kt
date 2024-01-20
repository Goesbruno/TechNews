package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import br.com.alura.technews.R
import br.com.alura.technews.databinding.ActivityVisualizaNoticiaBinding
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

    }



    private fun configuraFragment() {

    }




}
