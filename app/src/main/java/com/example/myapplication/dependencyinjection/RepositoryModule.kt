package com.example.myapplication.dependencyinjection

import com.example.myapplication.repositories.EditImageRepository
import com.example.myapplication.repositories.EditImageRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module



val repositoryModule= module{
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext())}


}