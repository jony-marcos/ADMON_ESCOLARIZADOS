/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Utils.Constants;
import itt.web.conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import mx.edu.ittoluca.logutils.Logger;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Jony
 */
public class InsercionesDAO {

    public static String insertRenovacioPref(String username, String password) {
        int codError;
        String msjError;
        Logger logger = new Logger();
        Connection conn = Conexion.getConnection(username, password, Constants.NOMBRE_APP, Constants.NOMBRE_MODULO);
        if (conn != null) {
            try {
                CallableStatement cs = conn.prepareCall("{call FICHAS.PQ_CHECK_ADMIN_1.GENERA_PETICION_CORREO_REN_SP(?,?)}");
                cs.registerOutParameter("paCodigoError", OracleTypes.NUMBER);
                cs.registerOutParameter("paMjeDescError", OracleTypes.VARCHAR);
                cs.execute();
                codError = cs.getInt("paCodigoError");
                if (codError == 0) {
                    msjError = "correcto";
                } else {
                    msjError = cs.getString("paMjeDescError");
                    String logMessage = codError + "->" + msjError;
                    logger.registrarError(Logger.GRAVE, logMessage, Constants.NOMBRE_APP, Constants.NOMBRE_MODULO, username);
                }
                cs.close();

            } catch (SQLException ex) {
                //Loggeo del error.
                logger.registrarErrorSQL(ex, Constants.NOMBRE_APP, Constants.NOMBRE_MODULO, username);
                msjError = logger.getMensajeError();
                //Gestión de la respuesta para el usuario.
                //Se obtiene la traducción del error con: logger.getMensajeError();

            } finally {
                //El bloque finally es importante pues aquí se garantiza que no se dejen conexiones abiertas.
                Conexion.cerrarConexion(conn);
            }
        } else {
            //Sólo se gestiona la respuesta que se dará al usuario, la librería ya loguea los errores al crear la conexión.
            //El error traducido está en Conexion.getConnectionErrorMessage();
            msjError = Conexion.getConnectionErrorMessage();
        }
        return msjError;
    }

    public static int altaCENEVAL(String username, String password, String idAspirante) {
        int ceneval = -4;
        int codError;
        String msjError;
        Logger logger = new Logger();
        Connection conn = Conexion.getConnection(username, password, Constants.NOMBRE_APP, Constants.NOMBRE_MODULO);
        if (conn != null) {
            try {
                CallableStatement cs = conn.prepareCall("{call FICHAS.PQ_CHECK_ADMIN_1.GENERA_ALTA_CENEVAL_SP(?,?,?)}");
                cs.setString("paIdAspirante", idAspirante);
                cs.registerOutParameter("paCodigoError", OracleTypes.NUMBER);
                cs.registerOutParameter("paMjeDescError", OracleTypes.VARCHAR);
                cs.execute();
                codError = cs.getInt("paCodigoError");
                if (codError == 0) {
                    ceneval = codError;
                } else {
                    msjError = cs.getString("paMjeDescError");
                    String logMessage = codError + "->" + msjError;
                    logger.registrarError(Logger.GRAVE, logMessage, Constants.NOMBRE_APP, Constants.NOMBRE_MODULO, username);
                    ceneval = (-1);
                }
                cs.close();

            } catch (SQLException ex) {
                //Loggeo del error.
                logger.registrarErrorSQL(ex, Constants.NOMBRE_APP, Constants.NOMBRE_MODULO, username);
                ceneval = (-2);
                //Gestión de la respuesta para el usuario.
                //Se obtiene la traducción del error con: logger.getMensajeError();

            } finally {
                //El bloque finally es importante pues aquí se garantiza que no se dejen conexiones abiertas.
                Conexion.cerrarConexion(conn);
            }
        } else {
            //Sólo se gestiona la respuesta que se dará al usuario, la librería ya loguea los errores al crear la conexión.
            //El error traducido está en Conexion.getConnectionErrorMessage();
            ceneval = (-3);
        }
        return ceneval;

    }
}
