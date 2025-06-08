package dev.yonel.services.controllers.dashboard;

public enum TYPE_CLOUD {
    CONECTED(1, "Conected"),
    DISCONECTED(2, "Disconected"), 
    CLOUD_CROSS(3, "Cloud Cross"),
    CLOUD_SYNC(4, "Cloud Sync"), 
    DOWNLOAD(5, "Download"),
    UPLOAD(6, "Upload"),
    CLOUD(7, "Cloud");


    private final int number;
    private final String type;

    TYPE_CLOUD(int number, String type){
        this.number = number;
        this.type = type;
    }

    public int getNumber(){
        return number;
    }

    public String getType(){
        return type;
    }

    public static TYPE_CLOUD fromNumber(int number){
        for(TYPE_CLOUD typeCloud: TYPE_CLOUD.values()){
            if(typeCloud.getNumber() == number){
                return typeCloud;
            }
        }
        throw new IllegalArgumentException("Invalid type number: " + number);
    }

}
