package com.boostech.demo;

import com.boostech.demo.controller.ProductController;
import com.boostech.demo.dto.GetOneProductDto;
import com.boostech.demo.repository.CategoryRepository;
import com.boostech.demo.repository.IAttributeRepository;
import com.boostech.demo.repository.PValueRepository;
import com.boostech.demo.repository.ProductRepository;
import com.boostech.demo.service.IPValueService;
import com.boostech.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class DemoApplicationTests {
	@Test
	void contextLoads() {

	}

}
