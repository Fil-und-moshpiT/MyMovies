package com.filundmoshpit.mymovies

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationBarView
import java.lang.ref.WeakReference

object ModifiedNavigationUI {
    /**
     * Modified to use with NavigationBarView.
     * Sets up a {@link NavigationBarView} for use with a {@link NavController}. This will call
     * {@link #onNavDestinationSelected(MenuItem, NavController)} when a menu item is selected. The
     * selected item in the BottomNavigationView will automatically be updated when the destination
     * changes.
     *
     * @param navigationView The NavigationBarView that should be kept in sync with
     *                             changes to the NavController.
     * @param navigationController The NavController that supplies the primary menu.
     *                      Navigation actions on this NavController will be reflected in the
     *                      selected item in the BottomNavigationView.
     */
    fun setupWithNavController(navigationView: NavigationBarView, navigationController: NavController) {
        navigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navigationController)
        }

        val weakReference = WeakReference(navigationView)
        navigationController.addOnDestinationChangedListener { controller, destination, arguments ->
            //Set checked element
            val view = weakReference.get()
            if (view != null) {
                val menu = view.menu
                var h = 0
                val size = menu.size()

                while (h < size) {
                    val item = menu.getItem(h)
                    if (matchDestination(destination, item.itemId)) {
                        item.isChecked = true
                    }
                    h++
                }
            }
        }
    }

    private fun matchDestination(destination: NavDestination, @IdRes destId: Int): Boolean {
        var currentDestination = destination

        while (currentDestination.id != destId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent!!
        }

        return currentDestination.id == destId
    }
}
