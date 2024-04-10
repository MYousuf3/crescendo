package com.example.crescendo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.LinkedList;


@IgnoreExtraProperties
public class Archive {
    LinkedList<Wrap> wraps;

    public Archive(LinkedList<Wrap> wraps) {
        this.wraps = wraps;
    }

    public LinkedList<Wrap> getWraps() {
        return wraps;
    }
}
