package com.example.proj.data

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    
    fun addFavorite(id: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(id)
        prefs.edit().putStringSet("favorite_ids", favorites).apply()
    }
    
    fun removeFavorite(id: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(id)
        prefs.edit().putStringSet("favorite_ids", favorites).apply()
    }
    
    fun isFavorite(id: String): Boolean {
        return getFavorites().contains(id)
    }
    
    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
    }
}
