package art.bangmarcel.gameplanner

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform