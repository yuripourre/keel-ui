package com.prodec.keel.ui;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.prodec.keel.ui.filter.ColorFilterView;

public class ValidationViewTest {

	private ColorFilterView filterView;
	private ValidationView view;

	@Before
	public void setUp() {
		view = new ValidationView(0, 0, 100, 70);

		filterView = new ColorFilterView(0, 0);
		view.link(filterView, new PipelineComponentItem(0, false), new PipelineComponentItem(1, true));
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
		
		//link view->anotherView
		view.link(anotherView, new PipelineComponentItem(0, false), new PipelineComponentItem(0, true));
		Assert.assertEquals(2, filterView.filter.getSearchStrategy().getValidations().size());

		Assert.assertEquals(anotherView, view.next);
	}

}
