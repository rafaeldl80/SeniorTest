import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
 
@Path("/cityservice")
public class CityService {
	@GET
	@Path("/listarPorCapitais")
	@Produces("application/json")
	public Response listarPorCapitais() throws SQLException 
	{
		ConnectionDB con = new ConnectionDB();
		Statement stmt = con.getConnection().createStatement();
        ResultSet rs;
        List<Cidade> cidades = new ArrayList<Cidade>();
        
        rs = stmt.executeQuery("SELECT * FROM Cidades c WHERE c.capital = 1 ORDER BY c.name ASC");
        while ( rs.next() ) 
        {
        	Cidade cidade = new Cidade();
        	cidade.setIbge_id(Integer.parseInt(rs.getString("ibge_id")));
        	cidade.setUf(rs.getString("uf"));
        	cidade.setName(rs.getString("name"));
        	if(rs.getString("capital").contentEquals("1")) cidade.setCapital(true);
        	else cidade.setCapital(false);
            cidade.setLon(Float.parseFloat(rs.getString("lon").replaceAll(",", ".")));
            cidade.setLat(Float.parseFloat(rs.getString("lat").replaceAll(",", ".")));
            cidade.setAlternative_names(rs.getString("alternative_names"));
            cidade.setMicroregion(rs.getString("microregion"));
            cidade.setMesoregion(rs.getString("mesoregion"));
            
            cidades.add(cidade);
        }
        
		return Response.status(200).entity(cidades).build();
	}
	
	@GET
	@Path("/listarEstadosMaxMinCitys")
	@Produces("application/json")
	public Response listarEstadosMaxMinCitys() throws SQLException 
	{
		ConnectionDB con = new ConnectionDB();
		Statement stmt = con.getConnection().createStatement();
        ResultSet rs;
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> listaJson = new ArrayList<JSONObject>();
        
        rs = stmt.executeQuery("DECLARE @tmp TABLE (Uf varchar(2), Qtd smallint) INSERT INTO @tmp SELECT uf, COUNT(*) AS Qtd FROM Cidades GROUP BY uf SELECT * FROM @tmp WHERE Qtd = (SELECT MAX(Qtd) FROM @tmp) OR Qtd = (SELECT MIN(Qtd) FROM @tmp)");
        while ( rs.next() ) 
        {
        	String uf = rs.getString("Uf");
        	int qtde = Integer.parseInt(rs.getString("Uf"));
        	jsonObject.put("Estado", uf); 
    		jsonObject.put("Quantidade", qtde);
    		
    		listaJson.add(jsonObject);
        }
        
		return Response.status(200).entity(listaJson).build();
	}
	
	@GET
	@Path("/listarQtdeCidadesEstados")
	@Produces("application/json")
	public Response listarQtdeCidadesEstados() throws SQLException 
	{
		ConnectionDB con = new ConnectionDB();
		Statement stmt = con.getConnection().createStatement();
        ResultSet rs;
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> listaJson = new ArrayList<JSONObject>();
        
        rs = stmt.executeQuery("SELECT c.uf, COUNT(c.name) AS qtde FROM Cidades c GROUP BY c.uf ORDER BY c.uf ASC");
        while ( rs.next() ) 
        {
        	String uf = rs.getString("uf");
        	String qtde = rs.getString("qtde");
        	jsonObject.put("Estado", uf); 
    		jsonObject.put("Quantidade", qtde);
    		
    		listaJson.add(jsonObject);
        }
        
		return Response.status(200).entity(listaJson).build();
	}
 
 
	  @Path("/listarPorIbgeId/{id}")
	  @GET
	  @Produces("application/json")
	  public Response listarPorIbgeId(@PathParam("id") int var) throws JSONException, SQLException {
	
		  	ConnectionDB con = new ConnectionDB();
			Statement stmt = con.getConnection().createStatement();
	        ResultSet rs;
	        String param = String.valueOf(var);
	        Cidade cidade = new Cidade();
	        
	        rs = stmt.executeQuery("SELECT * FROM Cidades c WHERE c.ibge_id = " + param);
	        while ( rs.next() ) 
	        {
	        	cidade.setIbge_id(Integer.parseInt(rs.getString("ibge_id")));
	        	cidade.setUf(rs.getString("uf"));
	        	cidade.setName(rs.getString("name"));
	        	if(rs.getString("capital").contentEquals("1")) cidade.setCapital(true);
	        	else cidade.setCapital(false);
	            cidade.setLon(Float.parseFloat(rs.getString("lon").replaceAll(",", ".")));
	            cidade.setLat(Float.parseFloat(rs.getString("lat").replaceAll(",", ".")));
	            cidade.setAlternative_names(rs.getString("alternative_names"));
	            cidade.setMicroregion(rs.getString("microregion"));
	            cidade.setMesoregion(rs.getString("mesoregion"));
	        }
	        
			return Response.status(200).entity(cidade).build();
	  }
	  
	  @Path("/listarCidadesPorEstado/{uf}")
	  @GET
	  @Produces("application/json")
	  public Response listarCidadesPorEstado(@PathParam("uf") String var) throws JSONException, SQLException {
	
		  	ConnectionDB con = new ConnectionDB();
			Statement stmt = con.getConnection().createStatement();
	        ResultSet rs;
	        List<Cidade> cidades = new ArrayList<Cidade>();
	        
	        rs = stmt.executeQuery("SELECT c.name FROM Cidades c WHERE c.uf = " + var);
	        while ( rs.next() ) 
	        {
	        	Cidade cidade = new Cidade();
	        	cidade.setIbge_id(Integer.parseInt(rs.getString("ibge_id")));
	        	cidade.setUf(rs.getString("uf"));
	        	cidade.setName(rs.getString("name"));
	        	if(rs.getString("capital").contentEquals("1")) cidade.setCapital(true);
	        	else cidade.setCapital(false);
	            cidade.setLon(Float.parseFloat(rs.getString("lon").replaceAll(",", ".")));
	            cidade.setLat(Float.parseFloat(rs.getString("lat").replaceAll(",", ".")));
	            cidade.setAlternative_names(rs.getString("alternative_names"));
	            cidade.setMicroregion(rs.getString("microregion"));
	            cidade.setMesoregion(rs.getString("mesoregion"));
	            
	            cidades.add(cidade);
	        }
	        
			return Response.status(200).entity(cidades).build();
	  }
	  
	  @Path("/adicionarCidade/{ibgeid/uf/name/capital/lon/lat/altname/micro/meso}")
	  @GET
	  @Produces("application/json")
	  public Response adicionarCidade(@PathParam("ibgeid") int ibgeid, @PathParam("uf") String uf, 
			  @PathParam("capital") boolean isCap, @PathParam("lon") float lon, @PathParam("lat") float lat, @PathParam("noacc") String noacc,
			  @PathParam("altname") String altname, @PathParam("micro") String micro, @PathParam("meso") String meso) throws JSONException, SQLException {
	
		  	ConnectionDB con = new ConnectionDB();
	        
	     // the mysql insert statement
	        String query = "INSERT INTO Cidades (ibge_id, uf, name, capital, lon, lat, no_accents, alternative_names, microregion, mesoregion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        // create the mysql insert preparedstatement
	        PreparedStatement preparedStmt = con.getConnection().prepareStatement(query);
	        preparedStmt.setInt(1, ibgeid);
	        preparedStmt.setString(2, uf);
	        preparedStmt.setBoolean(3, isCap);
	        preparedStmt.setFloat(4, lon);
	        preparedStmt.setFloat(5, lat);
	        preparedStmt.setString(6, noacc);
	        preparedStmt.setString(7, altname);
	        preparedStmt.setString(8, micro);
	        preparedStmt.setString(9, meso);
	        
	        // execute the preparedstatement
	        preparedStmt.execute();
	        
			return Response.status(200).build();
	  }
	  
	  @Path("/removerCidade/{ibgeid}")
	  @GET
	  @Produces("application/json")
	  public Response removerCidade(@PathParam("ibgeid") int ibgeid) throws JSONException, SQLException {
	
		  	ConnectionDB con = new ConnectionDB();
	        
	     // the mysql insert statement
	        String query = "DELETE FROM Cidades WHERE @ibge_id = " + ibgeid;

	        // create the mysql insert preparedstatement
	        PreparedStatement preparedStmt = con.getConnection().prepareStatement(query);
	        preparedStmt.setInt(1, ibgeid);
	        
	        // execute the preparedstatement
	        preparedStmt.execute();
	        
			return Response.status(200).build();
	  }
	  
	  @Path("/retornarQtdeRegPorCol/{col}")
	  @GET
	  @Produces("application/json")
	  public Response retornarQtdeRegPorCol(@PathParam("col") String var) throws JSONException, SQLException {
	
		  	ConnectionDB con = new ConnectionDB();
			Statement stmt = con.getConnection().createStatement();
	        ResultSet rs;
	        JSONObject jsonObject = new JSONObject();
	        
	        rs = stmt.executeQuery("SELECT COUNT(DISTINCT " + var + ") AS Qtde FROM Cidades");
	        while ( rs.next() ) 
	        {
	        	jsonObject.put("Quantidade", rs.getString("Qtde"));
	        }
	        
			return Response.status(200).entity(jsonObject).build();
	  }
	  
	    @GET
		@Path("/retornarQtdeRegistros")
		@Produces("application/json")
		public Response retornarQtdeRegistros() throws SQLException 
		{
			ConnectionDB con = new ConnectionDB();
			Statement stmt = con.getConnection().createStatement();
	        ResultSet rs;
	        List<JSONObject> listaJson = new ArrayList<JSONObject>();
	        
	        rs = stmt.executeQuery("DECLARE @tmp TABLE (Uf varchar(2),Cidade varchar(50), latitude float, longitude float, Conta float) INSERT INTO @tmp SELECT UF, name, lat, lon, lat + lon as conta FROM Cidades SELECT * FROM @tmp WHERE Conta = (SELECT MAX(conta) FROM @tmp) OR Conta = (SELECT MIN(conta) FROM @tmp)");
	        while ( rs.next() ) 
	        {
	        	JSONObject jsonObject = new JSONObject();
	    		jsonObject.put("Estado", rs.getString("Uf"));	
	    		jsonObject.put("Cidade", rs.getString("Cidade"));	
	    		jsonObject.put("Latitude", rs.getString("latitude"));	
	    		jsonObject.put("Longitude", rs.getString("longitude"));	
	    		jsonObject.put("Conta", rs.getString("Conta"));	
	    		
	    		listaJson.add(jsonObject);
	        }
	        
			return Response.status(200).entity(listaJson).build();
		}
}
