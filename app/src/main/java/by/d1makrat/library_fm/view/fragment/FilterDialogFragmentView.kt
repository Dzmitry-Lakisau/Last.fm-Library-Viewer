package by.d1makrat.library_fm.view.fragment

interface FilterDialogFragmentView {
    fun returnToTargetFragment(startOfPeriod: Long, endOfPeriod: Long)
    fun showWrongInputMessage()
}
