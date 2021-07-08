package org.acme.conference.session;

import javax.json.bind.annotation.JsonbCreator;
import java.util.UUID;

public class SpeakerFromService {
    private String uuid;
    private String nameFirst;
    private String nameLast;

    public SpeakerFromService(String uuid, String nameFirst, String nameLast) {
        this.uuid = uuid;
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
    }

    @JsonbCreator
    public static SpeakerFromService of(String uuid,String nameFirst, String nameLast){
        return new SpeakerFromService(uuid,nameFirst,nameLast);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }
}
