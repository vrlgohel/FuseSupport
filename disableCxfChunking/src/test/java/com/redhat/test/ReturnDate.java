package com.redhat.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Created by Viral Gohel
 */
@XmlType(name = "fun")
public class ReturnDate {
    private Date date = null;

    @XmlElement(nillable=false, required=true)
    public Date getDate()
    {
        return this.date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
