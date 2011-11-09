/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author ivano
 */
public class StaticAnalysisMetric extends QuantitativeMetric {
    
    // <editor-fold desc="Members">
    
    public File m_oInFile;
    public SoftwareConstruction m_oInLanguage;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public File GetInFile() {
        return m_oInFile;
    }

    public void SetInFile(File oInFile) {
        m_oInFile = oInFile;
    }

    public SoftwareConstruction GetInLanguage() {
        return m_oInLanguage;
    }

    public void SetInLanguage(SoftwareConstruction oInLanguage) {
        m_oInLanguage = oInLanguage;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public StaticAnalysisMetric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}