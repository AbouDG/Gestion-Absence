package sn.uasz.gestionConge.gestionConge.Service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class ReportService {

    public byte[] generateReport(String templateName, Map<String, Object> parameters) throws JRException {
        InputStream reportStream = getClass().getResourceAsStream("/reports/" + templateName + ".jasper");

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        return baos.toByteArray();
    }
}


