package net.d80harri.wr.service.mapping;

import org.springframework.stereotype.Component;

import net.d80harri.wr.db.model.Item;
import net.d80harri.wr.service.model.ItemDto;
import ma.glasnost.orika.CustomMapper;

@Component
public class ItemMapper extends CustomMapper<Item, ItemDto> {

}
