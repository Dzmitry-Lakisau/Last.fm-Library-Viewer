package by.d1makrat.library_fm.view.fragment

import by.d1makrat.library_fm.model.FilterRange

interface FilterDialogFragmentView {
    fun returnToTargetFragment(filterRange: FilterRange)
    fun showWrongInputMessage()
}
