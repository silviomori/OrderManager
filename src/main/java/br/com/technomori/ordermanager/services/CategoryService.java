package br.com.technomori.ordermanager.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.technomori.ordermanager.domain.Category;
import br.com.technomori.ordermanager.repositories.CategoryRepository;
import br.com.technomori.ordermanager.services.exceptions.ObjectNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	public Category fetch(Integer id) throws ObjectNotFoundException {
		
		Optional<Category> ret = repository.findById(id);

		return ret.orElseThrow(
			() -> new ObjectNotFoundException("Object not found: TYPE: "+Category.class.getName()+", ID: "+id)
		);

	}
}
