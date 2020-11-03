package htw.PlayerManagementImpl;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerManagementDao;
import htw.PlayerManagementInter.PlayerManagementServiceException;
import htw.PlayerManagementInter.PlayerManagement_Service;
import htw.PlayerManagementInter.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

public class PlayerManagement_Service_Test {
		
	private PlayerManagementDao pmDao;
	private PlayerManagement_Service playerManagementService;
	private	String playerName1 = "Georg";
	private String playerName2 = "Sascha";
	
	@Before
	public void setUp() {
		pmDao = Mockito.mock(PlayerManagementDao.class);
		playerManagementService = new PlayerManagement_Service_impl(pmDao);

	}
	
	/**
	 * Die Methode testet, ob eine Player erstellt wird 
	 * und in der DB anhand der PlayerID wieder gefunden werden kann
	 * @throws DuplicatePlayerException 
	 * @throws PlayerNotFoundException 
	 * @throws PlayerManagementServiceException 
	 */
	@Test
	public void testCreatePlayer() throws DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException {
		Player playerExpected = new Player(playerName1);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(null);
		Mockito.when(pmDao.getPlayer(playerExpected.getId())).thenReturn(playerExpected);
		
		Player player = playerManagementService.createPlayer(playerName1);
		Player playerTest = pmDao.getPlayer(playerExpected.getId());
		
		Assert.assertEquals(playerExpected, player);
		Assert.assertEquals(playerExpected, playerTest);
	}
	
	/**
	 * Die Methode testet, ob eine DuplicatePlayerException geworfen wird, wenn
	 * zwei Player mit dem selben Namen erstellt werden.
	 * @throws DuplicatePlayerException 
	 * @throws PlayerManagementServiceException 
	 * 
	 */
	@Test(expected = DuplicatePlayerException.class)
	public void testDuplicateCreatePlayer() throws DuplicatePlayerException {
		
		Player playerExpected = new Player(playerName1);
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(playerExpected);
		playerManagementService.createPlayer(playerName1);
	}
	
	/**
	 * Die Methode testet, ob die deletePlayer Methode
	 * einen Spieler theoretisch erfolgreich löscht
	 * 
	 * @throws DuplicatePlayerException
	 * @throws PlayerManagementServiceException
	 * @throws PlayerNotFoundException
	 */
	@Test
	public void testDeletePlayerSuccess() throws DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException {
		
		Mockito.when(pmDao.getPlayerByName(playerName2)).thenReturn(null);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		Player player = playerManagementService.createPlayer(playerName2);
		
		Mockito.when(pmDao.getPlayer(player.getId())).thenReturn(player);
		Mockito.when(pmDao.deletePlayer(player)).thenReturn(true);
		
		boolean result = playerManagementService.deletePlayer(player.getId());
		
		Assert.assertTrue(result);
	}
	
	/**
	 * Die Methode Testet, ob eine PlayerNotFoundException geworden wird,
	 * wenn beim Aufruf der deletePlayer Methode der Player nicht existiert 
	 * 
	 * @throws PlayerManagementServiceException
	 * @throws PlayerNotFoundException
	 */
	@Test(expected = PlayerNotFoundException.class)
	public void testDeletePlayerFails() throws PlayerManagementServiceException, PlayerNotFoundException {
		int unknownPlayerID = 1;
		Mockito.when(pmDao.getPlayer(unknownPlayerID)).thenReturn(null);
		playerManagementService.deletePlayer(unknownPlayerID);
	}
	
	/**
	 * Die Methode testet, ob ein Player erfolgreich editiert werden kann
	 * 
	 * @throws DuplicatePlayerException
	 * @throws PlayerManagementServiceException
	 * @throws PlayerNotFoundException
	 */
	@Test
	public void testEditPlayerSuccess() throws DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException {
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(null);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		
		Player player = playerManagementService.createPlayer(playerName1);
		
		Mockito.when(pmDao.getPlayer(player.getId())).thenReturn(player);
		Mockito.when(pmDao.getPlayerByName(playerName2)).thenReturn(null);
		
		player = playerManagementService.editPlayerName(player, playerName2);
		
		Mockito.when(pmDao.getPlayer(player.getId())).thenReturn(player);
		
		Player modifiedPlayer = pmDao.getPlayer(player.getId());
		
		
		Assert.assertEquals(playerName2, player.getName());
		Assert.assertEquals(playerName2, modifiedPlayer.getName());
		Assert.assertEquals(player, modifiedPlayer);
	}
	
	/**
	 * Die Methode testet, ob eine DuplicatePlayerException geworfen wird, 
	 * wenn beim Aufruf der editPlayer mMthode ein Player mit dem selben Namen bereits existiert
	 * 
	 * @throws DuplicatePlayerException
	 * @throws PlayerManagementServiceException
	 * @throws PlayerNotFoundException
	 */
	@Test(expected= DuplicatePlayerException.class )
	public void testEditPlayerFails() throws DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException {
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(null);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		Player player = playerManagementService.createPlayer(playerName1);
		Mockito.when(pmDao.getPlayerByName(playerName2)).thenReturn(player);
		player = playerManagementService.editPlayerName(player, playerName2);
	}
	
	/**
	 * Die Methode testet, ob eine PlayerNotFoundException geworfen wird, 
	 * wenn beim Aufruf der editPlayer Methode der zu editierende Player in der DB nicht mehr existerit
	 * 
	 * @throws DuplicatePlayerException
	 * @throws PlayerManagementServiceException
	 * @throws PlayerNotFoundException
	 */
	@Test(expected= PlayerNotFoundException.class )
	public void testEditPlayerFails2() throws DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException {
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(null);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		Player player = playerManagementService.createPlayer(playerName1);
		Mockito.when(pmDao.getPlayerByName(playerName2)).thenReturn(null);
		Mockito.when(pmDao.getPlayer(player.getId())).thenReturn(null);
		player = playerManagementService.editPlayerName(player, playerName2);
	}
	
	/**
	 * Die Methode testet, ob eine DuplicatePlayerException geworfen wird, 
	 * wenn beim Aufruf der editPlayer mMthode ein Player mit dem selben Namen bereits existiert
	 * 
	 * @throws DuplicatePlayerException
	 * @throws PlayerManagementServiceException
	 * @throws PlayerNotFoundException
	 */
	@Test(expected = DuplicatePlayerException.class)
	public void testEditPlayerFailes() throws DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException {
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(null);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		Player player = playerManagementService.createPlayer(playerName1);
		Mockito.when(pmDao.getPlayerByName("Test")).thenReturn(player);
		
		playerManagementService.editPlayerName(null, "Test");
	}
	
	/**
	 * Die Methode testet, ob alle Spieler beim Aufruf der 
	 * getAllPlayer Methode zurückgegeben werden
	 * 
	 * @throws DuplicatePlayerException
	 */
	@Test
	public void testGetAllPlayers() throws DuplicatePlayerException {
		List<Player> playerListExpect = new ArrayList<Player>();
		String[] playerNames = {playerName1, playerName2};
		
		Mockito.when(pmDao.getPlayerByName(playerName1)).thenReturn(null);
		Mockito.when(pmDao.getPlayerByName(playerName2)).thenReturn(null);
		Mockito.when(pmDao.savePlayer(Mockito.any(Player.class))).thenReturn(true);
		
		for(String playerName : playerNames) {
			playerListExpect.add(playerManagementService.createPlayer(playerName));
		}
		
		Mockito.when(pmDao.getAllPlayers()).thenReturn(playerListExpect);
		List<Player> playerList = playerManagementService.getAllPlayer();
		Assert.assertEquals(playerListExpect, playerList);
	}
	
}
