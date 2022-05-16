package academy.kafka.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import academy.kafka.utils.KentekenGenerator;
import academy.kafka.utils.RocksDbUtils;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.Car") })
@JsonSchemaDescription(value = "The car as a taxable object")
@JsonSchemaTitle(value = "Car")
public class Car extends Entity {
  
    private String kenteken;
    private String make;
    private String model;
    private Integer tax;// yust to play with, on yearly basis

    public Car() {
    }

    public Car(String kenteken, String make, String model) {
        this.kenteken = kenteken;
        this.make = make;
        this.model = model;
        this.tax = 1200;
    }

    public String getKey(){
        return kenteken;
    }

    public String getKenteken() {
        return this.kenteken;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }
        
    @Override
    public String toString() {
        return String.format("Car [kenteken=%s, make= %s, model=%s, tax=%s]\n",getKenteken() ,make,model,tax);
    }

    
    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/
    static public final String topicName = Car.class.getSimpleName();   

    public static Car fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Car.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    } 

    static String cars=
   "2020,Acura,ILX,[Sedan]\n"
+"2020,Acura,MDX,[SUV]\n"
+"2020,Acura,MDX Sport Hybrid,[SUV]\n"
+"2020,Acura,NSX,[Coupe]\n"
+"2020,Acura,RDX,[SUV]\n"
+"2020,Acura,RLX,[Sedan]\n"
+"2020,Acura,RLX Sport Hybrid,[Sedan]\n"
+"2020,Acura,TLX,[Sedan]\n"
+"2020,Alfa Romeo,4C Spider,[Convertible]\n"
+"2020,Alfa Romeo,Giulia,[Sedan]\n"
+"2020,Alfa Romeo,Stelvio,[SUV]\n"
+"2020,Audi,A3,[Sedan]\n"
+"2020,Audi,A4,[Sedan]\n"
+"2020,Audi,A5,[Coupe,Convertible,Sedan]\n"
+"2020,Audi,A6,[Sedan]\n"
+"2020,Audi,A6 allroad,[Wagon]\n"
+"2020,Audi,A7,[Sedan]\n"
+"2020,Audi,Q3,[SUV]\n"
+"2020,Audi,Q5,[SUV]\n"
+"2020,Audi,Q7,[SUV]\n"
+"2020,Audi,Q8,[SUV]\n"
+"2020,Audi,R8,[Coupe, Convertible]\n"
+"2020,Audi,S4,[Sedan]\n"
+"2020,Audi,S8,[Sedan]\n"
+"2020,Audi,SQ5,[SUV]\n"
+"2020,Audi,TT,[Coupe]\n"
+"2020,BMW,2 Series,[Coupe,Sedan,Convertible]\n"
+"2020,BMW,3 Series,[Sedan]\n"
+"2020,BMW,4 Series,[Sedan, Coupe, Convertible]\n"
+"2020,BMW,5 Series,[Sedan]\n"
+"2020,BMW,7 Series,[Sedan]\n"
+"2020,BMW,8 Series,[Coupe,Convertible,Sedan]\n"
+"2020,BMW,M2,[Coupe]\n"
+"2020,BMW,X1,[SUV]\n"
+"2020,BMW,X2,[SUV]\n"
+"2020,BMW,X3,[SUV]\n"
+"2020,BMW,X3 M,[SUV]\n"
+"2020,BMW,X4,[SUV]\n"
+"2020,BMW,X5,[SUV]\n"
+"2020,BMW,X6,[SUV]\n"
+"2020,BMW,X7,[SUV]\n"
+"2020,BMW,Z4,[Convertible]\n"
+"2020,Buick,Enclave,[SUV]\n"
+"2020,Buick,Encore,[SUV]\n"
+"2020,Buick,Encore GX,[SUV]\n"
+"2020,Buick,Envision,[SUV]\n"
+"2020,Buick,Regal Sportback,[Sedan]\n"
+"2020,Buick,Regal TourX,[Wagon]\n"
+"2020,Cadillac,CT4,[Sedan]\n"
+"2020,Cadillac,CT5,[Sedan]\n"
+"2020,Cadillac,CT6,[Sedan]\n"
+"2020,Cadillac,CT6-V,[Sedan]\n"
+"2020,Cadillac,Escalade,[SUV]\n"
+"2020,Cadillac,Escalade ESV,[SUV]\n"
+"2020,Cadillac,XT4,[SUV]\n"
+"2020,Cadillac,XT5,[SUV]\n"
+"2020,Cadillac,XT6,[SUV]\n"
+"2020,Chevrolet,Blazer,[SUV]\n"
+"2020,Chevrolet,Bolt EV,[Hatchback]\n"
+"2020,Chevrolet,Camaro,[Convertible, Coupe]\n"
+"2020,Chevrolet,Colorado Crew Cab,[Pickup]\n"
+"2020,Chevrolet,Colorado Extended Cab,[Pickup]\n"
+"2020,Chevrolet,Corvette,[Coupe, Convertible]\n"
+"2020,Chevrolet,Equinox,[SUV]\n"
+"2020,Chevrolet,Express 2500 Cargo,[Van/Minivan]\n"
+"2020,Chevrolet,Express 3500 Cargo,[Van/Minivan]\n"
+"2020,Chevrolet,Impala,[Sedan]\n"
+"2020,Chevrolet,Malibu,[Sedan]\n"
+"2020,Chevrolet,Silverado 1500 Crew Cab,[Pickup]\n"
+"2020,Chevrolet,Silverado 1500 Double Cab,[Pickup]\n"
+"2020,Chevrolet,Silverado 1500 Regular Cab,[Pickup]\n"
+"2020,Chevrolet,Silverado 2500 HD Crew Cab,[Pickup]\n"
+"2020,Chevrolet,Silverado 2500 HD Double Cab,[Pickup]\n"
+"2020,Chevrolet,Silverado 3500 HD Crew Cab,[Pickup]\n"
+"2020,Chevrolet,Sonic,[Sedan,Hatchback]\n"
+"2020,Chevrolet,Spark,[Hatchback]\n"
+"2020,Chevrolet,Suburban,[SUV]\n"
+"2020,Chevrolet,Tahoe,[SUV]\n"
+"2020,Chevrolet,Traverse,[SUV]\n"
+"2020,Chevrolet,Trax,[SUV]\n"
+"2020,Chrysler,300,[Sedan]\n"
+"2020,Chrysler,Pacifica,[Van/Minivan]\n"
+"2020,Chrysler,Pacifica Hybrid,[Van/Minivan]\n"
+"2020,Chrysler,Voyager,[Van/Minivan]\n"
+"2020,Dodge,Challenger,[Coupe]\n"
+"2020,Dodge,Charger,[Sedan]\n"
+"2020,Dodge,Durango,[SUV]\n"
+"2020,Dodge,Grand Caravan Passenger,[Van/Minivan]\n"
+"2020,Dodge,Journey,[SUV]\n"
+"2020,FIAT,124 Spider,[Convertible]\n"
+"2020,FIAT,500L,[Hatchback]\n"
+"2020,FIAT,500X,[SUV]\n"
+"2020,Ford,EcoSport,[SUV]\n"
+"2020,Ford,Edge,[SUV]\n"
+"2020,Ford,Escape,[SUV]\n"
+"2020,Ford,Expedition,[SUV]\n"
+"2020,Ford,Expedition MAX,[SUV]\n"
+"2020,Ford,Explorer,[SUV]\n"
+"2020,Ford,F150 Regular Cab,[Pickup]\n"
+"2020,Ford,F150 Super Cab,[Pickup]\n"
+"2020,Ford,F150 SuperCrew Cab,[Pickup]\n"
+"2020,Ford,F250 Super Duty Crew Cab,[Pickup]\n"
+"2020,Ford,F250 Super Duty Regular Cab,[Pickup]\n"
+"2020,Ford,F250 Super Duty Super Cab,[Pickup]\n"
+"2020,Ford,F350 Super Duty Crew Cab,[Pickup]\n"
+"2020,Ford,F350 Super Duty Super Cab,[Pickup]\n"
+"2020,Ford,F450 Super Duty Crew Cab,[Pickup]\n"
+"2020,Ford,Fusion,[Sedan]\n"
+"2020,Ford,Fusion Plug-in Hybrid,[Sedan]\n"
+"2020,Ford,Mustang,[Coupe, Convertible]\n"
+"2020,Ford,Ranger SuperCab,[Pickup]\n"
+"2020,Ford,Ranger SuperCrew,[Pickup]\n"
+"2020,Ford,Transit 250 Cargo Van,[Van/Minivan]\n"
+"2020,Ford,Transit 350 Passenger Van,[Van/Minivan]\n"
+"2020,Ford,Transit Connect Cargo Van,[Van/Minivan]\n"
+"2020,Ford,Transit Connect Passenger Wagon,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 1500 Cargo,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 1500 Passenger,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 2500 Cargo,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 2500 Crew,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 2500 Passenger,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 3500 Cargo,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 3500 Crew,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 3500 XD Crew,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 3500XD Cargo,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 4500 Cargo,[Van/Minivan]\n"
+"2020,Freightliner,Sprinter 4500 Crew,[Van/Minivan]\n"
+"2020,Genesis,G70,[Sedan]\n"
+"2020,Genesis,G80,[Sedan]\n"
+"2020,Genesis,G90,[Sedan]\n"
+"2020,GMC,Acadia,[SUV]\n"
+"2020,GMC,Canyon Crew Cab,[Pickup]\n"
+"2020,GMC,Sierra 1500 Crew Cab,[Pickup]\n"
+"2020,GMC,Sierra 1500 Double Cab,[Pickup]\n"
+"2020,GMC,Sierra 1500 Regular Cab,[Pickup]\n"
+"2020,GMC,Sierra 2500 HD Crew Cab,[Pickup]\n"
+"2020,GMC,Sierra 3500 HD Crew Cab,[Pickup]\n"
+"2020,GMC,Terrain,[SUV]\n"
+"2020,GMC,Yukon,[SUV]\n"
+"2020,GMC,Yukon XL,[SUV]\n"
+"2020,Honda,Accord,[Sedan]\n"
+"2020,Honda,Accord Hybrid,[Sedan]\n"
+"2020,Honda,Civic,[Hatchback, Coupe, Sedan]\n"
+"2020,Honda,Civic Type R,[Hatchback]\n"
+"2020,Honda,Clarity Fuel Cell,[Sedan]\n"
+"2020,Honda,Clarity Plug-in Hybrid,[Sedan]\n"
+"2020,Honda,CR-V,[SUV]\n"
+"2020,Honda,CR-V Hybrid,[SUV]\n"
+"2020,Honda,Fit,[Hatchback]\n"
+"2020,Honda,HR-V,[SUV]\n"
+"2020,Honda,Insight,[Sedan]\n"
+"2020,Honda,Odyssey,[Van/Minivan]\n"
+"2020,Honda,Passport,[SUV]\n"
+"2020,Honda,Pilot,[SUV]\n"
+"2020,Honda,Ridgeline,[Pickup]\n"
+"2020,Hyundai,Accent,[Sedan]\n"
+"2020,Hyundai,Elantra,[Sedan]\n"
+"2020,Hyundai,Elantra GT,[Hatchback]\n"
+"2020,Hyundai,Ioniq Electric,[Hatchback]\n"
+"2020,Hyundai,Ioniq Hybrid,[Hatchback]\n"
+"2020,Hyundai,Ioniq Plug-in Hybrid,[Hatchback]\n"
+"2020,Hyundai,Kona,[SUV]\n"
+"2020,Hyundai,Kona Electric,[SUV]\n"
+"2020,Hyundai,NEXO,[SUV]\n"
+"2020,Hyundai,Palisade,[SUV]\n"
+"2020,Hyundai,Santa Fe,[SUV]\n"
+"2020,Hyundai,Sonata,[Sedan]\n"
+"2020,Hyundai,Sonata Hybrid,[Sedan]\n"
+"2020,Hyundai,Tucson,[SUV]\n"
+"2020,Hyundai,Veloster,[Coupe]\n"
+"2020,Hyundai,Venue,[SUV]\n"
+"2020,INFINITI,Q50,[Sedan]\n"
+"2020,INFINITI,Q60,[Coupe]\n"
+"2020,INFINITI,QX50,[SUV]\n"
+"2020,INFINITI,QX60,[SUV]\n"
+"2020,INFINITI,QX80,[SUV]\n"
+"2020,Jaguar,E-PACE,[SUV]\n"
+"2020,Jaguar,F-PACE,[SUV]\n"
+"2020,Jaguar,F-TYPE,[Coupe, Convertible]\n"
+"2020,Jaguar,I-PACE,[SUV]\n"
+"2020,Jaguar,XE,[Sedan]\n"
+"2020,Jaguar,XF,[Sedan, Wagon]\n"
+"2020,Jeep,Cherokee,[SUV]\n"
+"2020,Jeep,Compass,[SUV]\n"
+"2020,Jeep,Gladiator,[Pickup]\n"
+"2020,Jeep,Grand Cherokee,[SUV]\n"
+"2020,Jeep,Renegade,[SUV]\n"
+"2020,Jeep,Wrangler,[SUV]\n"
+"2020,Jeep,Wrangler Unlimited,[SUV]\n"
+"2020,Kia,Cadenza,[Sedan]\n"
+"2020,Kia,Forte,[Sedan]\n"
+"2020,Kia,K900,[Sedan]\n"
+"2020,Kia,Niro,[Wagon]\n"
+"2020,Kia,Niro EV,[Wagon]\n"
+"2020,Kia,Niro Plug-in Hybrid,[Wagon]\n"
+"2020,Kia,Optima,[Sedan]\n"
+"2020,Kia,Optima Hybrid,[Sedan]\n"
+"2020,Kia,Optima Plug-in Hybrid,[Sedan]\n"
+"2020,Kia,Rio,[Sedan, Hatchback]\n"
+"2020,Kia,Sedona,[Van/Minivan]\n"
+"2020,Kia,Sorento,[SUV]\n"
+"2020,Kia,Soul,[Wagon]\n"
+"2020,Kia,Sportage,[SUV]\n"
+"2020,Kia,Stinger,[Sedan]\n"
+"2020,Kia,Telluride,[SUV]\n"
+"2020,Land Rover,Defender 110,[SUV]\n"
+"2020,Land Rover,Defender 90,[SUV]\n"
+"2020,Land Rover,Discovery,[SUV]\n"
+"2020,Land Rover,Discovery Sport,[SUV]\n"
+"2020,Land Rover,Range Rover,[SUV]\n"
+"2020,Land Rover,Range Rover Evoque,[SUV]\n"
+"2020,Land Rover,Range Rover Sport,[SUV]\n"
+"2020,Land Rover,Range Rover Velar,[SUV]\n"
+"2020,Lexus,ES,[Sedan]\n"
+"2020,Lexus,GS,[Sedan]\n"
+"2020,Lexus,GX,[SUV]\n"
+"2020,Lexus,IS,[Sedan]\n"
+"2020,Lexus,LC,[Coupe]\n"
+"2020,Lexus,LS,[Sedan]\n"
+"2020,Lexus,LX,[SUV]\n"
+"2020,Lexus,NX,[SUV]\n"
+"2020,Lexus,RC,[Coupe]\n"
+"2020,Lexus,RX,[SUV]\n"
+"2020,Lexus,UX,[SUV]\n"
+"2020,Lincoln,Aviator,[SUV]\n"
+"2020,Lincoln,Continental,[Sedan]\n"
+"2020,Lincoln,Corsair,[SUV]\n"
+"2020,Lincoln,MKZ,[Sedan]\n"
+"2020,Lincoln,Nautilus,[SUV]\n"
+"2020,Lincoln,Navigator,[SUV]\n"
+"2020,Lincoln,Navigator L,[SUV]\n"
+"2020,MAZDA,CX-3,[SUV]\n"
+"2020,MAZDA,CX-30,[SUV]\n"
+"2020,MAZDA,CX-5,[SUV]\n"
+"2020,MAZDA,CX-9,[SUV]\n"
+"2020,MAZDA,MAZDA3,[Sedan, Hatchback]\n"
+"2020,MAZDA,MAZDA6,[Sedan]\n"
+"2020,MAZDA,MX-5 Miata,[Convertible]\n"
+"2020,MAZDA,MX-5 Miata RF,[Convertible]\n"
+"2020,Mercedes-Benz,A-Class,[Sedan]\n"
+"2020,Mercedes-Benz,C-Class,[Sedan, Convertible, Coupe]\n"
+"2020,Mercedes-Benz,CLA,[Sedan]\n"
+"2020,Mercedes-Benz,CLS,[Sedan]\n"
+"2020,Mercedes-Benz,E-Class,[Sedan, Coupe, Wagon, Convertible]\n"
+"2020,Mercedes-Benz,G-Class,[SUV]\n"
+"2020,Mercedes-Benz,GLA,[SUV]\n"
+"2020,Mercedes-Benz,GLB,[SUV]\n"
+"2020,Mercedes-Benz,GLC,[SUV]\n"
+"2020,Mercedes-Benz,GLE,[SUV]\n"
+"2020,Mercedes-Benz,GLS,[SUV]\n"
+"2020,Mercedes-Benz,Mercedes-AMG A-Class,[Sedan]\n"
+"2020,Mercedes-Benz,Mercedes-AMG C-Class,[Sedan, Coupe, Convertible]\n"
+"2020,Mercedes-Benz,Mercedes-AMG CLS,[Sedan]\n"
+"2020,Mercedes-Benz,Mercedes-AMG GLC,[SUV]\n"
+"2020,Mercedes-Benz,Mercedes-AMG GLC Coupe,[SUV]\n"
+"2020,Mercedes-Benz,Metris WORKER Cargo,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Metris WORKER Passenger,[Van/Minivan]\n"
+"2020,Mercedes-Benz,S-Class,[Sedan, Coupe, Convertible]\n"
+"2020,Mercedes-Benz,SLC,[Convertible]\n"
+"2020,Mercedes-Benz,Sprinter 1500 Cargo,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 1500 Passenger,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 2500 Cargo,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 2500 Crew,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 2500 Passenger,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 3500 Cargo,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 3500 Crew,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 3500 XD Cargo,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 3500 XD Crew,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 4500 Cargo,[Van/Minivan]\n"
+"2020,Mercedes-Benz,Sprinter 4500 Crew,[Van/Minivan]\n"
+"2020,MINI,Clubman,[Hatchback]\n"
+"2020,MINI,Countryman,[SUV]\n"
+"2020,MINI,Hardtop 2 Door,[Hatchback]\n"
+"2020,MINI,Hardtop 4 Door,[Hatchback]\n"
+"2020,Mitsubishi,Eclipse Cross,[SUV]\n"
+"2020,Mitsubishi,Mirage,[Hatchback]\n"
+"2020,Mitsubishi,Mirage G4,[Sedan]\n"
+"2020,Mitsubishi,Outlander,[SUV]\n"
+"2020,Mitsubishi,Outlander PHEV,[SUV]\n"
+"2020,Mitsubishi,Outlander Sport,[SUV]\n"
+"2020,Nissan,370Z,[Coupe]\n"
+"2020,Nissan,Altima,[Sedan]\n"
+"2020,Nissan,Armada,[SUV]\n"
+"2020,Nissan,Frontier Crew Cab,[Pickup]\n"
+"2020,Nissan,Frontier King Cab,[Pickup]\n"
+"2020,Nissan,GT-R,[Coupe]\n"
+"2020,Nissan,Kicks,[SUV]\n"
+"2020,Nissan,LEAF,[Hatchback]\n"
+"2020,Nissan,Maxima,[Sedan]\n"
+"2020,Nissan,Murano,[SUV]\n"
+"2020,Nissan,NV1500 Cargo,[Van/Minivan]\n"
+"2020,Nissan,NV200,[Van/Minivan]\n"
+"2020,Nissan,NV2500 HD Cargo,[Van/Minivan]\n"
+"2020,Nissan,NV3500 HD Cargo,[Van/Minivan]\n"
+"2020,Nissan,NV3500 HD Passenger,[Van/Minivan]\n"
+"2020,Nissan,Pathfinder,[SUV]\n"
+"2020,Nissan,Rogue,[SUV]\n"
+"2020,Nissan,Rogue Sport,[SUV]\n"
+"2020,Nissan,Sentra,[Sedan]\n"
+"2020,Nissan,Titan Crew Cab,[Pickup]\n"
+"2020,Nissan,Titan King Cab,[Pickup]\n"
+"2020,Nissan,TITAN XD Crew Cab,[Pickup]\n"
+"2020,Nissan,Versa,[Sedan]\n"
+"2020,Porsche,718 Boxster,[Convertible]\n"
+"2020,Porsche,718 Cayman,[Coupe]\n"
+"2020,Porsche,718 Spyder,[Convertible]\n"
+"2020,Porsche,911,[Convertible, Coupe]\n"
+"2020,Porsche,Cayenne,[SUV]\n"
+"2020,Porsche,Cayenne Coupe,[SUV]\n"
+"2020,Porsche,Macan,[SUV]\n"
+"2020,Porsche,Panamera,[Sedan]\n"
+"2020,Porsche,Taycan,[Sedan]\n"
+"2020,Ram,1500 Classic Crew Cab,[Pickup]\n"
+"2020,Ram,1500 Classic Quad Cab,[Pickup]\n"
+"2020,Ram,1500 Crew Cab,[Pickup]\n"
+"2020,Ram,1500 Quad Cab,[Pickup]\n"
+"2020,Ram,2500 Crew Cab,[Pickup]\n"
+"2020,Ram,3500 Crew Cab,[Pickup]\n"
+"2020,Ram,ProMaster Cargo Van,[Van/Minivan]\n"
+"2020,Ram,ProMaster City,[Van/Minivan]\n"
+"2020,Subaru,Ascent,[SUV]\n"
+"2020,Subaru,BRZ,[Coupe]\n"
+"2020,Subaru,Crosstrek,[SUV]\n"
+"2020,Subaru,Forester,[SUV]\n"
+"2020,Subaru,Impreza,[Wagon, Sedan]\n"
+"2020,Subaru,Legacy,[Sedan]\n"
+"2020,Subaru,Outback,[SUV]\n"
+"2020,Subaru,WRX,[Sedan]\n"
+"2020,Tesla,Model 3,[Sedan]\n"
+"2020,Tesla,Model S,[Sedan]\n"
+"2020,Tesla,Model X,[SUV]\n"
+"2020,Tesla,Model Y,[SUV]\n"
+"2020,Toyota,4Runner,[SUV]\n"
+"2020,Toyota,86,[Coupe]\n"
+"2020,Toyota,Avalon,[Sedan]\n"
+"2020,Toyota,Avalon Hybrid,[Sedan]\n"
+"2020,Toyota,Camry,[Sedan]\n"
+"2020,Toyota,Camry Hybrid,[Sedan]\n"
+"2020,Toyota,C-HR,[SUV]\n"
+"2020,Toyota,Corolla,[Sedan]\n"
+"2020,Toyota,Corolla Hatchback,[Hatchback]\n"
+"2020,Toyota,Corolla Hybrid,[Sedan]\n"
+"2020,Toyota,GR Supra,[Coupe]\n"
+"2020,Toyota,Highlander,[SUV]\n"
+"2020,Toyota,Highlander Hybrid,[SUV]\n"
+"2020,Toyota,Land Cruiser,[SUV]\n"
+"2020,Toyota,Mirai,[Sedan]\n"
+"2020,Toyota,Prius,[Hatchback]\n"
+"2020,Toyota,Prius Prime,[Hatchback]\n"
+"2020,Toyota,RAV4,[SUV]\n"
+"2020,Toyota,RAV4 Hybrid,[SUV]\n"
+"2020,Toyota,Sequoia,[SUV]\n"
+"2020,Toyota,Sienna,[Van/Minivan]\n"
+"2020,Toyota,Tacoma Access Cab,[Pickup]\n"
+"2020,Toyota,Tacoma Double Cab,[Pickup]\n"
+"2020,Toyota,Tundra CrewMax,[Pickup]\n"
+"2020,Toyota,Tundra Double Cab,[Pickup]\n"
+"2020,Toyota,Yaris,[Sedan]\n"
+"2020,Toyota,Yaris Hatchback,[Hatchback]\n"
+"2020,Volkswagen,Arteon,[Sedan]\n"
+"2020,Volkswagen,Atlas,[SUV]\n"
+"2020,Volkswagen,Atlas Cross Sport,[SUV]\n"
+"2020,Volkswagen,Golf,[Hatchback]\n"
+"2020,Volkswagen,Golf GTI,[Hatchback]\n"
+"2020,Volkswagen,Jetta,[Sedan]\n"
+"2020,Volkswagen,Jetta GLI,[Sedan]\n"
+"2020,Volkswagen,Passat,[Sedan]\n"
+"2020,Volkswagen,Tiguan,[SUV]\n"
+"2020,Volvo,S60,[Sedan]\n"
+"2020,Volvo,S90,[Sedan]\n"
+"2020,Volvo,V60,[Wagon]\n"
+"2020,Volvo,V90,[Wagon]\n"
+"2020,Volvo,XC40,[SUV]\n"
+"2020,Volvo,XC60,[SUV]\n"
+"2020,Volvo,XC90,[SUV]\n"
    ;
    public static Set<String> generateDatabase(int aantal) {
        Set<String> kentekens = KentekenGenerator.generateKentekenSet(aantal);
        Iterator<String> kentekenIterator = kentekens.iterator();
        RocksDB db = RocksDbUtils.newDatabase(Car.topicName);
       
             while (kentekenIterator.hasNext()) {
               try {
                   try (BufferedReader br = new BufferedReader(new StringReader(cars))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");                          
                            String kenteken = kentekenIterator.next();
                            Car car = new Car(kenteken, values[1], values[2]);
                            db.put(kenteken.getBytes(), car.toJson().getBytes());
                            if (!kentekenIterator.hasNext())
                                break;
                        }
                    }
                } catch ( IOException | RocksDBException e) {
                    e.printStackTrace();
                }
            }
        
        db.close();
        return kentekens;
    }
}
