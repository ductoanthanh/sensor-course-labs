package com.example.mibeo.w1d35_president_retrofit

class President(var name: String, var startTime: Int, var endTime: Int, var description: String) : Comparable<President> {
    override fun compareTo(other: President): Int {
        return Integer.compare(this.startTime, other.startTime)
    }

    override fun toString(): String {
        return "​$name ($startTime -​ $endTime)​"
    }
}