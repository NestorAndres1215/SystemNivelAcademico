package com.example.utils;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "notas")
public class ImportacionNotasWrapper {

    private List<NotaXML> notas = new ArrayList<>(); // inicializar para evitar null

    @XmlElement(name = "nota")
    public List<NotaXML> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaXML> notas) {
        if (notas == null) {
            this.notas = new ArrayList<>();
        } else {
            this.notas = notas;
        }
    }
}
