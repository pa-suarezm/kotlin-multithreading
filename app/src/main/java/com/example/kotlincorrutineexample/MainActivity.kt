/**
 * ISIS-3510 Mobile App Development
 * Universidad de Los Andes
 * Based on the article "Multithreading and Kotlin" by Korhan Bircan
 * https://medium.com/@korhanbircan/multithreading-and-kotlin-ac28eed57fea
 * @author pa-suarezm
 */

package com.example.kotlincorrutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincorrutineexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityMainBinding

    //For progress bars
    private val maxCnt = 2_500_000

    private lateinit var viewModel: MainViewModel;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //viewBinding to replace findViewById
        //https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = MainViewModel(binding)

        //Initialize sequential section
        binding.btnSequential.setOnClickListener {
            viewModel.sequentialFunction()
        }
        binding.progressBarSeq1.max = maxCnt
        binding.progressBarSeq2.max = maxCnt

        //Initialize concurrent section
        binding.btnConcurrent.setOnClickListener {
            viewModel.concurrentFunction()
        }
        binding.progressBarCon1.max = maxCnt
        binding.progressBarCon2.max = maxCnt
    }




}