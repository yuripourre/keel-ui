package com.prodec.keel.ui;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.prodec.keel.ui.filter.ColorFilterView;

import br.com.etyllica.motion.filter.ColorFilter;

public class ValidationViewTest {

	private ColorFilter filter;
	private ColorFilterView filterView;

	private ValidationView view;

	@Before
	public void setUp() {
		filter = new ColorFilter(10, 10);
		view = new ValidationView(0, 0, 100, 70);

		filterView = new ColorFilterView(0, 0, filter);
		view.link(filterView);
	}

	@Test
	public void testCreation() {
		Assert.assertNull(view.next);
		Assert.assertNull(view.previous);
	}

	@Test
	public void testLinkValidationView() {
		Assert.assertEquals(filterView, view.filterView);
		Assert.assertEquals(1, filterView.filter.getSearchStrategy().getValidations().size());
		
		ValidationView anotherView = new ValidationView(0, 0, 100, 70);
		view.link(anotherView);
		Assert.assertEquals(2, filterView.filter.getSearchStrategy().getValidations().size());

		Assert.assertEquals(anotherView, view.next);
	}

}
