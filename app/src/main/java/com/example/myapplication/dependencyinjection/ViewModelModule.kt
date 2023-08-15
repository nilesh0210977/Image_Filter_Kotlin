package com.example.myapplication.dependencyinjection

import com.example.myapplication.viewmodels.EditImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
}