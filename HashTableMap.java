import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableMap<KeyType, ValueType>
    implements MapADT<KeyType, ValueType> {


    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    }


    protected LinkedList<Pair>[] table;

    private int size;


    @SuppressWarnings("unchecked")
    public HashTableMap(int capacity) {

        table = (LinkedList<Pair>[]) new LinkedList[capacity];

        for(int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<Pair>();
        }

        size = 0;
    }


    public HashTableMap() {

        this(64);

    }


    public void put(KeyType key, ValueType value) {

        if(key == null) {
            throw new NullPointerException();
        }


        int index = Math.abs(key.hashCode()) % table.length;


        for(Pair pair : table[index]) {

            if(pair.key.equals(key)) {
                throw new IllegalArgumentException();
            }

        }


        table[index].add(new Pair(key,value));

        size++;


        if((double)size / table.length >= 0.75) {

            resize();

        }

    }



    public boolean containsKey(KeyType key) {


        if(key == null) {
            return false;
        }


        int index = Math.abs(key.hashCode()) % table.length;


        for(Pair pair : table[index]) {

            if(pair.key.equals(key)) {

                return true;

            }

        }


        return false;

    }



    public ValueType get(KeyType key) {


        int index = Math.abs(key.hashCode()) % table.length;


        for(Pair pair : table[index]) {


            if(pair.key.equals(key)) {

                return pair.value;

            }

        }


        throw new NoSuchElementException();

    }



    public ValueType remove(KeyType key) {


        int index = Math.abs(key.hashCode()) % table.length;


        for(Pair pair : table[index]) {


            if(pair.key.equals(key)) {


                ValueType value = pair.value;

                table[index].remove(pair);

                size--;

                return value;

            }

        }


        throw new NoSuchElementException();

    }



    public void clear() {


        for(int i = 0; i < table.length; i++) {

            table[i].clear();

        }


        size = 0;

    }



    public int getSize() {

        return size;

    }



    public int getCapacity() {

        return table.length;

    }



    public List<KeyType> getKeys() {


        List<KeyType> keys = new LinkedList<KeyType>();


        for(int i = 0; i < table.length; i++) {


            for(Pair pair : table[i]) {

                keys.add(pair.key);

            }

        }


        return keys;

    }



    @SuppressWarnings("unchecked")
    private void resize() {


        LinkedList<Pair>[] oldTable = table;


        table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];


        for(int i = 0; i < table.length; i++) {

            table[i] = new LinkedList<Pair>();

        }


        for(int i = 0; i < oldTable.length; i++) {


            for(Pair pair : oldTable[i]) {


                int index =
                    Math.abs(pair.key.hashCode()) % table.length;


                table[index].add(pair);

            }

        }


    }



    // keep your five JUnit tests below if needed

}