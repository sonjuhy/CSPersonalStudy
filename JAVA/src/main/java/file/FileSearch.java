package file;

import sql.SQL;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearch {

    static List<FileDto> list = new ArrayList<>();

    public void compareToWalkAndDFS(){
        System.out.println("Compare start");

        long filesWalkStartTime = System.currentTimeMillis();
        filesWalk();
        long filesWalkEndTime = System.currentTimeMillis();
        System.out.println("filesWalk working time : "+((filesWalkEndTime - filesWalkStartTime)/1000));

        long fileBFSStartTime = System.currentTimeMillis();
        BFS();
        long fileBFSEndTime = System.currentTimeMillis();
        System.out.println("fileBFS working time : " +((fileBFSEndTime - fileBFSStartTime)/1000));

        long fileDFSStartTime = System.currentTimeMillis();
        filesDFS();
        long fileDFSEndTime = System.currentTimeMillis();
        System.out.println("fileDFS working time : " +((fileDFSEndTime - fileDFSStartTime)/1000));

        long fileDFSWithoutIOStartTime = System.currentTimeMillis();
        filesDFSWithoutIO();
        long fileDFSWithoutIOEndTime = System.currentTimeMillis();
        System.out.println("fileWithoutIODFS working time : " +((fileDFSWithoutIOEndTime - fileDFSWithoutIOStartTime)/1000));
    }
    public void filesWalk(){
        String originPath = "E:\\WorkSpace";

        Path dirPath = Paths.get(originPath);
        List<Path> listPath;
        try {
            Stream<Path> walk = Files.walk(dirPath);
            listPath = walk.collect(Collectors.toList());
            System.out.println("walk end. path size : "+listPath.size());
            for(Path path: listPath){
                File file = new File(path.toString());
//                saveToList(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void filesDFS(){
        String originPath = "E:\\WorkSpace";
        File defaultFile = new File(originPath);
        File[] files = defaultFile.listFiles();
        long size = files.length;
        for(File file : files){
            size += DFS(file.getPath(), 0);
        }
        System.out.println("filesDFS total size : "+size);
    }
    private int DFS(String path, int size){
        File defaultFile = new File(path);
        File[] files = defaultFile.listFiles();
        if(files != null) {
            for (File file : files) {
                size++;
                if (file.isDirectory()) {
                    size = DFS(file.getPath(), size);
                }
            }
        }
        return size;
    }
    public void filesDFSWithoutIO(){
        String originPath = "E:\\WorkSpace";
        long count = 0;
        for(FileDto file : list){
            if(file.isDirectory()) count++;
        }
        System.out.println(count);
        List<FileDto> subList = list
                .stream()
                .filter(file -> originPath.equals(file.getLocation())).collect(Collectors.toList());
        DFSWithOutIO(subList);
    }
    private void DFSWithOutIO(List<FileDto> fileList){
        for(FileDto dto : fileList){
            if(dto.isDirectory()){
                DFSWithOutIO(list
                        .stream()
                        .filter(file -> dto.getPath().equals(file.getLocation()))
                        .collect(Collectors.toList())
                );
            }
        }
        fileList.clear();
    }
    private void saveToList(File file){
        String pathStr = file.getPath();
        String[] pathStrArr = pathStr.split("\\\\");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i< pathStrArr.length-1;i++){
            sb.append(pathStrArr[i]);
            if(i < pathStrArr.length-2) sb.append(String.valueOf(File.separator));
        }
        String locationStr = sb.toString();
        list.add(new FileDto(pathStr, locationStr, file.isDirectory()));
    }

    public void BFS(){
        String originPath = "E:\\WorkSpace";
        Queue<File> queue = new LinkedList<>();
        File originFile = new File(originPath);
        File[] files = originFile.listFiles();
        for(File file : files){
            if(file.isDirectory()) queue.add(file);
        }
        while(!queue.isEmpty()){
            int qSize = queue.size();
            for(int i=0;i<qSize;i++){
                File file = queue.poll();
                File[] fileArr = file.listFiles();
                for(File tmpFile : fileArr){
                    if(tmpFile.isDirectory()) queue.add(tmpFile);
                }
            }
        }
    }
    private void saveToDB(File file, SQL sql){
        String path = file.getPath().replaceAll("\\\\", "__").replaceAll(":", "");
        String selectQuery = "SELECT id FROM file WHERE name = '"+path+"';";
        String insertQuery = "INSERT INTO file VALUE(0, '"+path+"', "+(file.length()/1024)+");";
        sql.saveFileInfo(selectQuery, insertQuery);
    }

    private void saveToDBVer2(File file, SQL sql){
//        String path = file.getPath().replaceAll("\\\\", "__").replaceAll(":", "");
        String path = UUID.nameUUIDFromBytes(file.getPath().getBytes(StandardCharsets.UTF_8)).toString();
        // User
        //insert into my_db.student(student_id, student_nm, email, phone) select 3, 301, 'email2@gmail.com', '010-4321-8756' from dual where not exists(select student_id from my_db.student where student_id=3);
        String query = "INSERT INTO file(id, name, file_size) SELECT "
                +"0, '"
                +path+"', "
                +(file.length()/1024)+
                " FROM DUAL WHERE NOT EXISTS(SELECT id FROM file WHERE name='"
                +path+"');";
        sql.excuteQuery(query);
    }
    private void countPercent(long total, long now){
        if(total%now == 0){
            System.out.println("process : "+(now/total)*100+"%");
        }
    }
}
