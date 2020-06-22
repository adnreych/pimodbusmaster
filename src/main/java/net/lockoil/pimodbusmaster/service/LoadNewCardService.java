package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.repository.CardRegisterElementRepository;

@Service
public class LoadNewCardService {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	private final CardRegisterElementRepository cardRegisterElementRepository;
	
	@Autowired
	public LoadNewCardService(CardRegisterElementRepository cardRegisterElementRepository) {
		this.cardRegisterElementRepository = cardRegisterElementRepository;
	}
	
	public ArrayList<CardRegisterElement> saveRegisters(ArrayList<CardRegisterElement> cardRegisterElements) {
		cardRegisterElementRepository.saveAll(cardRegisterElements);
		return cardRegisterElements;
	}

}
