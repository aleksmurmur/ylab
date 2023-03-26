package hw3.orgstructure;

import java.io.File;
import java.io.IOException;

public class OrgStructureTest {

    public static void main(String[] args) throws IOException {
        File csvFile = new File("src/main/java/hw3/orgstructure/csvs/validCsv");
        OrgStructureParser osp = new OrgStructureParserImpl();
        Employee boss = osp.parseStructure(csvFile);

        System.out.println(boss.getId());
        System.out.println(boss.getBossId());
        System.out.println(boss.getName());
        System.out.println(boss.getPosition());
        System.out.println(boss.getBoss());
        System.out.println(boss.getSubordinate());
    }
}
