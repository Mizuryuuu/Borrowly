package dev.dodo.borrowly.common.type

enum class Status(val label: String) {
    ALL("Semua"),
    PROSES("Proses"),
    BORROW("Dipinjam"),
    FINISH("Selesai"),
    REJECT("Ditolak"),
}