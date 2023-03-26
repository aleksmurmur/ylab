package hw3.orgstructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OrgStructureParserImplTest {

    private OrgStructureParser osp;
    @BeforeEach
    void setUp() {
        osp = new OrgStructureParserImpl();
    }


    @Test
    void parseStructure() throws IOException {
        File validCsv = new File("src/main/java/hw3/orgstructure/csvs/validCsv");
        File nonExistentFile = new File("non-existent/path/file");
        File notValidIdCsv = new File("src/main/java/hw3/orgstructure/csvs/notValidIdCsv");
        File notValidEmployeeParamsSizeCsv = new File("src/main/java/hw3/orgstructure/csvs/notValidEmployeeParamsSizeCsv");
        File notValidFirstLineCsv = new File("src/main/java/hw3/orgstructure/csvs/notValidFirstLineCsv");

        assertThrows(IOException.class, () -> osp.parseStructure(nonExistentFile));
        assertThrows(FileStructureNotValidException.class, () -> osp.parseStructure(notValidIdCsv));
        assertThrows(FileStructureNotValidException.class, () -> osp.parseStructure(notValidEmployeeParamsSizeCsv));
        assertThrows(FileStructureNotValidException.class, () -> osp.parseStructure(notValidFirstLineCsv));

        Employee boss = osp.parseStructure(validCsv);

        assertEquals(1, boss.getId());
        assertNull(boss.getBossId());
        assertNull(boss.getBoss());
        assertEquals( "Иван Иванович", boss.getName());
        assertEquals( "Генеральный директор", boss.getPosition());
        assertEquals( 4, boss.getSubordinate().size());
    }
}