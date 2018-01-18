package sample.com.fuad.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.com.fuad.backend.ListItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {

    private static String fileName= "Data.txt";
    private static TodoData onlyInstance= new TodoData();

    private ObservableList<ListItem> dataList;
    private DateTimeFormatter formater;

    private TodoData()
    {
        this.formater= DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<ListItem> getDataList() {
        return dataList;
    }

   /* public void setDataList(ObservableList<ListItem> dataList) {
        this.dataList = dataList;
    }
*/

    public void laodToDoList() throws IOException
    {
        dataList= FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader buff= Files.newBufferedReader(path);
        String input;
        try
        {
            while((input=buff.readLine())!=null)
            {
                String [] piece= input.split("\t");
                String shortDescrption= piece[0];
                String longDescrption= piece[1];
                String deadl= piece[2];

                LocalDate date= LocalDate.parse(deadl,formater);
                ListItem item = new ListItem(shortDescrption,longDescrption,date);
                dataList.add(item);
            }

        }
        finally
        {
            if(buff!=null) buff.close();
        }
    }

    public static TodoData getOnlyInstance() {
        return onlyInstance;
    }

    public void saveToDoList() throws IOException
    {
        Path path= Paths.get(fileName);
        BufferedWriter buff= Files.newBufferedWriter(path);

        try
        {
            Iterator<ListItem> iter = dataList.iterator();
            while(iter.hasNext())
            {
                ListItem item = iter.next();
                buff.write(item.getShortDescription()+"\t"+item.getLongDescription()+"\t"+item.getDeadLine().format(formater));
                buff.newLine();
            }
        }
        finally {
            if(buff!=null) buff.close();
        }
    }


    public void addToList(ListItem item)
    {
        dataList.add(item);
    }

    public void Delete(ListItem item)
    {
        dataList.remove(item);
    }
}
