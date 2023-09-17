package com.example.bpregister.domain

class BPComparator:Comparator<BPItem> {
    override fun compare(p0: BPItem?, p1: BPItem?): Int {
        if(p0 == null||p1 ==null) {return 0}
        else {
            if(p0.localDate.equals(p1.localDate)) {
                return p0.localTime.compareTo(p1.localTime)
            } else {
                return p0.localDate.compareTo(p1.localDate)
            }
        }
    }
}