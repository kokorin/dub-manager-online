package dmo.client.mapper {

import mx.collections.ArrayCollection;
import mx.logging.ILogger;
import mx.logging.Log;

import org.apache.royale.reflection.MetaDataArgDefinition;
import org.apache.royale.reflection.MetaDataDefinition;
import org.apache.royale.reflection.TypeDefinition;
import org.apache.royale.reflection.VariableDefinition;
import org.apache.royale.reflection.describeType;
import org.apache.royale.reflection.getDefinitionByName;

public class Mapper {
    private var typeDef:TypeDefinition;

    private static const LOGGER:ILogger = Log.getLogger("dmo.client.mapper.Mapper");

    public function Mapper(clazz:Class) {
        typeDef = describeType(clazz);
    }

    public function fromDto(dto:Object):Object {
        return convertObject(dto, typeDef);
    }

    private static function convertObject(dto:Object, typeDef:TypeDefinition):Object {
        LOGGER.debug("Converting Object");
        const clazz:Class = typeDef.getClass();
        const result:Object = new clazz();

        for each(var varDef:VariableDefinition in typeDef.variables) {
            if (varDef.isStatic) {
                LOGGER.debug("Skipped variable: {0} of {1}", varDef, typeDef);
                continue;
            }

            var value:Object = dto[varDef.name];

            const varClazz:Class = varDef.type.getClass();
            switch (varClazz) {
                case Number:
                    LOGGER.debug("Variable has Number type: {0} of {1}", varDef, typeDef);
                    break;
                case String:
                    LOGGER.debug("Variable has String type: {0} of {1}", varDef, typeDef);
                    break;
                case Array:
                    LOGGER.debug("Variable has Array type: {0} of {1}", varDef, typeDef);
                    value = convertArray(value as Array, varDef.metadata);
                    break;
                case ArrayCollection:
                    LOGGER.debug("Variable has ArrayCollection type: {0} of {1}", varDef, typeDef);
                    value = convertArrayCollection(value as Array, varDef.metadata);
                    break;
                case Date:
                    LOGGER.warn("Variable has Date type: {0} of {1}, {2}", varDef, typeDef);
                    //TODO date conversion
                    break;
                default:
                    LOGGER.warn("Variable has UNKNOWN type: {0} of {1}", varDef, typeDef);
                    break;
            }

            varDef.setValue(result, value);
        }

        return result;
    }

    private static function convertArrayCollection(array:Array, metadata:Array):ArrayCollection {
        LOGGER.debug("Converting ArrayCollection");
        const result:Array = convertArray(array, metadata);
        return new ArrayCollection(result);
    }

    private static function convertArray(array:Array, metadata:Array):Array {
        LOGGER.debug("Converting Array");

        var arrayElementDef:MetaDataDefinition = null;

        for each (var metaDef:MetaDataDefinition in metadata) {
            if (metaDef.name == "ArrayElementType") {
                arrayElementDef = metaDef;
                LOGGER.debug("ArrayElementType Metadata found: {0}", arrayElementDef);
                break;
            }
        }

        if (!arrayElementDef) {
            LOGGER.warn("Failed to get ArrayElementType Metadata: {0}", metadata);
            return array;
        }

        var arrayItemTypeName:String = null;
        for each (var arg:MetaDataArgDefinition in arrayElementDef.args) {
            if (arg.key == "" || arg.key == "elementType") {
                arrayItemTypeName = arg.value;
                LOGGER.debug("Element Type found: {0}, meta: {1}", arrayItemTypeName, metaDef);
                break;
            }
        }

        if (!arrayItemTypeName) {
            LOGGER.warn("Failed to detect Array Element Type name: {0}", metadata);
            return array;
        }

        const arrayItemTypeClazz:Class = getDefinitionByName(arrayItemTypeName) as Class;
        if (!arrayItemTypeClazz) {
            LOGGER.warn("Failed to get Class: {0}, got: {1}", arrayItemTypeName, arrayItemTypeClazz);
            return array;
        }

        const arrayItemTypeDef:TypeDefinition = describeType(arrayItemTypeClazz);

        if (!arrayItemTypeDef) {
            LOGGER.warn("Failed to get Type Definition: {0}, got: {1}", arrayItemTypeName, arrayItemTypeDef);
            return array;
        }

        LOGGER.debug("Array Element Type detected: {0}", arrayItemTypeDef);

        const result:Array = [];
        for each (var itemDto:Object in array) {
            var item:Object = convertObject(itemDto, arrayItemTypeDef);
            result.push(item);
        }

        return result;
    }
}
}
