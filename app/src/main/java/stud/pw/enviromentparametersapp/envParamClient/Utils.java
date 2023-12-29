package stud.pw.enviromentparametersapp.envParamClient;


import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;

import stud.pw.enviromentparametersapp.models.ArrayResponse;

public class Utils {
    static Type getArrayResponseType(Type genericType){
       return  $Gson$Types.newParameterizedTypeWithOwner(ArrayResponse.class, ArrayResponse.class, genericType);
    }
}
