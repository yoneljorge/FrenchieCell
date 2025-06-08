package dev.yonel.utils.fechaUtil;

/**
 * Enum que representa los meses del año, proporcionando tanto el nombre completo como la abreviatura de cada mes.
 */
public enum Meses {
    ENERO(1, "ENERO", "ENE"),
    FEBRERO(2, "FEBRERO", "FEB"),
    MARZO(3, "MARZO", "MAR"),
    ABRIL(4, "ABRIL", "ABR"),
    MAYO(5, "MAYO", "MAY"),
    JUNIO(6, "JUNIO", "JUN"),
    JULIO(7, "JULIO", "JUL"),
    AGOSTO(8, "AGOSTO", "AGO"), 
    SEPTIEMBRE(9, "SEPTIEMBRE", "SEP"),
    OCTUBRE(10, "OCTUBRE", "OCT"),
    NOVIEMBRE(11, "NOVIEMBRE", "NOV"),
    DICIEMBRE(12, "DICIEMBRE", "DIC");

    private final int number;
    private final String fullName;
    private final String shortName;

    /**
     * Constructor para inicializar los valores del enum Meses.
     *
     * @param number    El número del mes.
     * @param fullName  El nombre completo del mes.
     * @param shortName La abreviatura del mes.
     */
    Meses(int number, String fullName, String shortName){
        this.number = number;
        this.fullName = fullName;
        this.shortName = shortName;
    }

    /**
     * Obtiene el número del mes.
     *
     * @return El número del mes.
     */
    public int getNumber(){
        return number;
    }

    /**
     * Obtiene el nombre completo del mes.
     *
     * @return El nombre completo del mes.
     */
    public String getFullName(){
        return fullName;
    }

    /**
     * Obtiene la abreviatura del mes.
     *
     * @return La abreviatura del mes.
     */
    public String getShortName(){
        return shortName;
    }

    /**
     * Obtiene el mes correspondiente al número dado.
     *
     * @param number El número del mes.
     * @return El mes correspondiente.
     * @throws IllegalArgumentException si el número del mes es inválido.
     */
    public static Meses fromNumber(int number){
        for(Meses mes: Meses.values()){
            if(mes.getNumber() == number){
                return mes;
            }
        }
        throw new IllegalArgumentException("Número inválido de mes: " + number);
    }

}
